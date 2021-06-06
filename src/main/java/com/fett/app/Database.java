package com.fett.app;

import com.fett.app.models.Profile;
import com.fett.app.models.Task;
import com.fett.app.models.TaskStatus;
import com.fett.app.models.Video;
import com.fett.app.youtube.YouTubeChannel;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.firestore.*;
import com.google.common.base.Strings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ImplFirebaseTrampolines;

import java.time.Instant;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkArgument;

public class Database {
    private Firestore db;

    public Database(FirebaseApp app) {
        String projectId = ImplFirebaseTrampolines.getProjectId(app);
        checkArgument(!Strings.isNullOrEmpty(projectId),
                "Project ID is required for accessing Firestore. Use a service account credential or "
                        + "set the project ID explicitly via FirebaseOptions. Alternatively you can also "
                        + "set the project ID via the GOOGLE_CLOUD_PROJECT environment variable.");
        FirestoreOptions userOptions = ImplFirebaseTrampolines.getFirestoreOptions(app);
        FirestoreOptions.Builder builder = userOptions != null
                ? userOptions.toBuilder() : FirestoreOptions.newBuilder();
        this.db = builder
                // CredentialsProvider has highest priority in FirestoreOptions, so we set that.
                .setCredentialsProvider(
                        FixedCredentialsProvider.create(ImplFirebaseTrampolines.getCredentials(app)))
                .setProjectId(projectId)
                .build()
                .getService();
    }

    public Firestore getFirestore(){
        return db;
    }

    public void count(Task task, Video video, int time){
        try {
            final DocumentReference docRef = db.collection("taskresult")
                    .document(task.getId());
            ApiFuture<Void> futureTransaction = db.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(docRef).get();
                long oldcount = snapshot.getLong("viewcount");
                transaction.update(docRef, "viewcount", oldcount + 1);
                long oldtime = snapshot.getLong("timecount");
                transaction.update(docRef, "timecount", oldtime + time);
                return null;
            });
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void setFinished(Task task){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TaskStatus.finished);
        db.collection("task").document(task.getId()).update(map);
    }

    public Profile getProfile(String userid) throws Exception {
        ApiFuture<DocumentSnapshot> query = db.collection("profile").document(userid).get();
        Profile profile = query.get().toObject(Profile.class);
        profile.setId(query.get().getId());
        System.out.println("User: " + profile.getId());
        System.out.println("First: " + profile.getChannel());
        return profile;
    }

    public void addMonitoring(Video video, String id, String taskid, int watchlength){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", video.getTitle());
        map.put("watchlength", watchlength);
        map.put("start", Instant.now().toEpochMilli());
        db.collection("taskresult").document(taskid).collection("monitor").document(id).set(map);
    }
     public void clearMonitoring(String id, String taskid){
        db.collection("taskresult").document(taskid).collection("monitor").document(id).delete();

     }
    public void addStatistics(String channelid, YouTubeChannel stats, YouTubeChannel channel) {
        DocumentReference ref = db.collection("statistics")
                .document(channelid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("views", channel.getItems().get(0).getStatistics().getViewCount());
        map.put("subscribers", channel.getItems().get(0).getStatistics().getSubscriberCount());
        ref.set(map);

        stats.getItems().forEach((element) -> {
            DocumentReference refVideo = ref.collection("videos")
                    .document(element.getId());
            Map<String, Object> mapVideo = new HashMap<String, Object>();
            mapVideo.put("title", element.getSnippet().getTitle());
            refVideo.set(mapVideo);

            DocumentReference refStat = refVideo.collection("views")
                    .document();
            Map<String, Object> mapStat = new HashMap<String, Object>();
            mapStat.put("timestamp", Calendar.getInstance().getTimeInMillis());
            mapStat.put("views", element.getStatistics().getViewCount());
            refStat.set(mapStat);
        });
    }
}