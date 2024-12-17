package com.qromarck.reciperu.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class DataAccessUtilities {

    // Listener interfaces for callbacks
    public interface OnSavedListener {
        void onSaveCompleted();
        void onSaveError(String errorMessage);
    }

    public interface OnDataRetrievedListener<T> {
        void onDataRetrieved(List<T> data);
        void onError(String errorMessage);
    }

    public interface OnDeletedListener {
        void onDeleteComplete();
        void onDeleteError(String errorMessage);
    }

    /**
     * Inserts data into Firebase Realtime Database.
     */
    public static void insertOnFireStoreRealtime(String collectionName, String documentId, Map<String, Object> data,
                                                 OnSavedListener insertionListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(collectionName).child(documentId);
        databaseReference.setValue(data).addOnSuccessListener(unused -> {
            if (insertionListener != null) {
                insertionListener.onSaveCompleted();
            }
        }).addOnFailureListener(e -> {
            if (insertionListener != null) {
                insertionListener.onSaveError(e.getMessage());
                e.printStackTrace(System.err);
            }
        });
    }
}
