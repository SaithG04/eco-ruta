package com.qromarck.reciperu.callbacks;

public interface UserProfileImageCallback {
    void onUploadSuccess();                    // Called when profile image is successfully uploaded
    void onFetchSuccess(String imageString);   // Called when profile image is successfully fetched
    void onError(String errorMessage);         // Called for errors during image operations
}
