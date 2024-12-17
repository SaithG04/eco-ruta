package com.qromarck.reciperu.callbacks;

public interface UserDatabaseCallback {
    void onSuccess();                      // Called when user data is successfully saved
    void onError(String errorMessage);         // Called for errors during the save process
}