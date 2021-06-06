package com.fett.app;

import com.fett.app.models.*;
import com.fett.app.services.ChannelWorker;
import com.fett.app.services.DirectWorker;
import com.fett.app.services.PlaylistWorker;
import com.fett.app.services.SearchWorker;
import com.fett.app.utils.ProxyList;
import com.fett.app.youtube.YouTubeAPI;
import com.fett.app.youtube.YouTubeChannel;
import com.google.cloud.firestore.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class YoutubeBot {
//    private String apiKey = "RgSnuSLsXCNwwkxAUBrKFhBSRuX8JMbXdkn6tRGOP08";
    private String apiKey = "CzshUQMLepP63rTZHBYd95SKx7DwFqmn";
//    private String apiKey = "tfpwPGrbxhnyjJEVLqTU89B4cFdeM6mX";

    private ExecutorService executor;
    private Database db;
    private List<Task> tasks;
    private List<Video> videos;
    private boolean isFinished;
    private int viewCount = 0;

    public YoutubeBot(Database db){
        this.isFinished = false;
        this.db = db;
        tasks = new ArrayList<Task>();
        taskListener();
        //startStatistics();
    }

    private void startStatistics() {
        String channelid = "UCFQ037EnicEgpIOycjkiU8Q";
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running stats...");
                YouTubeAPI api = new YouTubeAPI("AIzaSyBJvswioN31bM7ZVU1PicqAl4e6Gs_ymfE");
                try {
                    YouTubeChannel channel = api.getChannel(channelid);
                    String uploadsId = channel.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads();
                    YouTubeChannel uploads = api.getPlaylist(uploadsId);
                    List<String> list = new ArrayList<String>();
                    uploads.getItems().forEach((element) -> {
                        list.add(element.getSnippet().getResourceId().getVideoId());
                    });
                    String s = String.join(",", list);
                    YouTubeChannel stats = api.getStats(s);
                    db.addStatistics(channelid, stats, channel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    private void taskListener(){
        try {
            db.getFirestore().collection("task")
                    .whereEqualTo("status", TaskStatus.running)
                    .orderBy("starttime")
                    .limit(1)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                            try {

                                if(!queryDocumentSnapshots.isEmpty()) {
                                    System.out.println("Starting tasks...");
                                    Task task = queryDocumentSnapshots.getDocuments().get(0).toObject(Task.class);
                                    videos = new ArrayList<Video>();
                                    db.getFirestore()
                                            .collection("task")
                                            .document(task.getId())
                                            .collection("videos")
                                            .get()
                                            .get().getDocuments().forEach(v -> {
                                        Video video = v.toObject(Video.class);
                                        videos.add(video);
                                        //videos.get(videos.size()-1).setId(v.getId());
                                    });
                                    isFinished = false;
                                    start(task, videos);
                                } else {
                                    System.out.println("Waiting for task...");
                                }
                            } catch (InterruptedException ex) {
                                System.out.println("1"+ex.getMessage());
                            } catch (ExecutionException ex) {
                                System.out.println("2"+ex.getMessage());
                            } catch (Exception ex) {
                                System.out.println("3"+ex.getMessage());
                            }
                        }
                    });
        } catch (FirestoreException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void finish(){
        isFinished = true;
        executor.shutdownNow();
        while (!executor.isTerminated()) { }
    }

    public void start(Task task, List<Video> videos) throws Exception {
        Random r = new Random();
        int workers = task.getSpeed();
        String type = task.getType();

        ViewCounter viewCounter = new ViewCounter(0);
        ProxyList proxyList = new ProxyList();
        try {
            while(!isFinished) {
                executor = Executors.newFixedThreadPool(workers);
                for (int i = 0; i < (task.getViews()-viewCount)/workers; i++) {
                    switch (type) {
                        case "direct": {
                            Runnable worker = new DirectWorker(
                                    "Worker:"+i,
                                    task,
                                    apiKey,
                                    db,
                                    viewCounter,
                                    proxyList,
                                    videos);
                            executor.execute(worker);
                            break;
                        }
                        case "search": {
                            Runnable worker = new SearchWorker(
                                    "Worker:"+i,
                                    task,
                                    apiKey,
                                    db,
                                    viewCounter,
                                    proxyList,
                                    videos);
                            executor.execute(worker);
                            break;
                        }
                        case "channel": {
                            Runnable worker = new ChannelWorker(
                                    "Worker:"+i,
                                    task,
                                    apiKey,
                                    db,
                                    viewCounter,
                                    proxyList,
                                    videos);
                            executor.execute(worker);
                            break;
                        }
                        case "playlist": {
                            Runnable worker = new PlaylistWorker(
                                    "Worker:"+i,
                                    task,
                                    apiKey,
                                    db,
                                    viewCounter,
                                    proxyList,
                                    videos);
                            executor.execute(worker);
                            break;
                        }
                    }
                }
                executor.shutdown();
                while (!executor.isTerminated()) {
                    if(viewCounter.getCount()>=task.getViews()){
                        isFinished = true;
                    }
                }
            }
            db.setFinished(task);
            System.out.println("Task finished...");
        } catch (IOException e) {
            System.out.println("IO");
        } catch (URISyntaxException e) {
            System.out.println("URI");
        }
    }
}