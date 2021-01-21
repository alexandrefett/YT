package com.fett.app;

import com.fett.app.models.Driver;
import com.fett.app.models.Profile;
import com.fett.app.models.Task;
import com.fett.app.services.BotWorker;
import com.google.cloud.firestore.*;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YoutubeBot {
    private String apiKey = "RgSnuSLsXCNwwkxAUBrKFhBSRuX8JMbXdkn6tRGOP08";
    private Driver driverType = Driver.CHROME;
    private ExecutorService executor;
    private Database db;
    private List<Task> tasks;

    public YoutubeBot(Database db){
        this.db = db;
        tasks = new ArrayList<Task>();
        taskListener();
    }

    private void taskListener(){
        CollectionReference ref = db.getFirestore().collection("task");
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                tasks.clear();
                queryDocumentSnapshots.getDocuments().forEach(doc->{
                    tasks.add(doc.toObject(Task.class));
                    tasks.get(tasks.size()-1).setId(doc.getId());
                });
                System.out.println("------------");
                tasks.forEach(ta->System.out.println(ta.getId()+" -- " +ta.getStatus()));
                Optional<Task> t = tasks.stream().filter(tk -> tk.getStatus().equals("running")).findFirst();
                if(!t.isPresent()){
                    Optional<Task> next = tasks.stream().filter(tk -> tk.getStatus().equals("queued")).findFirst();
                    if(next.isPresent()){
                        Task nextTask = next.get();
                        nextTask.setId(next.get().getId());
                        nextTask.setStatus("running");
                        db.getFirestore().collection("task").document(nextTask.getId()).set(nextTask);
                    }
                } else {
                    try {
                        System.out.println(t.get().getId()+" :: " +t.get().getUserid());
                        start(t.get());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public void finish(){
        executor.shutdownNow();
        while (!executor.isTerminated()) { }
    }

    public void start(Task task) throws Exception {
        Random r = new Random();
        int workers = task.getWorkers();
        try {
            Profile p = db.getProfile(task.getUserid());
/*            for(int i =0;i<task.getViews(); i++ ) {
                for(int j =0;j<p.getVideos().size(); j++ ) {
                    Thread.sleep(5000);
                    db.count(task.getUserid(), p.getVideos().get(j).getId(),r.nextInt(120));
                }
                System.out.println("view:" + i);
            }
            System.out.println("next task");


 */
            for (int count = 0;count<task.getViews(); count++ ) {
                executor = Executors.newFixedThreadPool(workers);
                for (int i = 0; i < task.getWorkers(); i++) {
                    Runnable worker = new BotWorker(
                            p,
                            task,
                            driverType,
                            apiKey,
                            db);
                    executor.execute(worker);
                }
                if(count + workers < task.getViews()){
                    count = count + workers;
                }
                executor.shutdown();
                //Wait until all threads are finish
                while (!executor.isTerminated()) { }
            }
            db.setFinished(task);
        } catch ( InterruptedException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
