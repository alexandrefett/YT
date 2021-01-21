package com.fett.app;

import com.fett.app.models.Profile;
import com.fett.app.models.Task;
import com.fett.app.models.Video;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.common.base.Strings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.ImplFirebaseTrampolines;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.annotation.Nullable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

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

    public void count(String userid, String videoid, int time){
        final DocumentReference docRef =
                db.collection("profile").document(userid).collection("videos").document(videoid);
        ApiFuture<Void> futureTransaction = db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(docRef).get();
            long oldcount = snapshot.getLong("count");
            transaction.update(docRef, "count", oldcount + 1);
            long oldtime = snapshot.getLong("time");
            transaction.update(docRef, "time", oldtime + time);
            return null;
        });
    }

    public void setFinished(Task task){
        task.setStatus("finished");
        db.collection("task").document(task.getId()).set(task);
    }

    public Profile getProfile(String userid) throws Exception {
        ApiFuture<DocumentSnapshot> query = db.collection("profile").document(userid).get();
        Profile profile = query.get().toObject(Profile.class);
        profile.setId(query.get().getId());
        System.out.println("User: " + profile.getId());
        System.out.println("First: " + profile.getChannel());
        ApiFuture<QuerySnapshot> queryVideos = db.collection("profile").document(userid).collection("videos").get();
        queryVideos.get().getDocuments().forEach(doc -> {
            profile.getVideos().add(doc.toObject(Video.class));
            profile.getVideos().get(profile.getVideos().size()-1).setId(doc.getId());
        });
        profile.getVideos().forEach(e -> System.out.println("Video: " + e.getTitle()));
        return profile;
    }

    private void addListener(){
       CollectionReference ref = db.collection("task");
        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirestoreException e) {
                queryDocumentSnapshots.getDocuments().forEach(doc->System.out.println(doc.getData().toString()));
            }
        });
    }

    void runQuery() throws Exception {
        ApiFuture<QuerySnapshot> query =
                db.collection("users").whereLessThan("born", 1900).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }
        // [END fs_add_query]
    }



}