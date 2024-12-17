package com.qromarck.reciperu.callbacks;

import org.json.JSONObject;

public interface ReniecCallback {
    void onSuccess(JSONObject response);       // Called when DNI data is successfully retrieved
    void onDniNotFound();                      // Called when DNI is not found
    void onError(String errorMessage);         // Called for network or API errors
}