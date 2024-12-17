package com.qromarck.reciperu.Utilities;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.qromarck.reciperu.controllers.ApiResponse;
import com.qromarck.reciperu.exception.ApiResponseException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorUtilities {
    public static <T> Exception getExceptionFromResponse(Response<ApiResponse<T>> response) {
        try (ResponseBody responseBody = response.errorBody()) {
            if (responseBody != null) {
                try {
                    String errorJson = responseBody.string();
                    Log.e("ERROR_BODY", errorJson); // Depuración del JSON recibido

                    // Convertir el JSON a JsonObject para depurar
                    JsonObject jsonObject = JsonParser.parseString(errorJson).getAsJsonObject();

                    // Extraer campos
                    int code = jsonObject.has("code") ? jsonObject.get("code").getAsInt() : -1;
                    String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "Unknown error";

                    return new ApiResponseException(code, message);
                } catch (IOException e) {
                    return new Exception("Error reading error response: " + e.getMessage());
                } catch (JsonSyntaxException e) {
                    return new Exception("JSON parsing error: " + e.getMessage());
                } catch (Exception e){
                    return new Exception("Unknown error: " + e.getMessage());
                }
            } else {
                return new Exception("No error body received.");
            }
        }
    }

    public static void validateExceptionType(Context context, Exception e){
        if(e instanceof ApiResponseException && ((ApiResponseException) e).getCode() != 500){
            InterfacesUtilities.showError(context, "API_EXCEPTION", "Server Error: " + e.getMessage(), e.getMessage());
        }else {
            InterfacesUtilities.showDefaultError(context, "API_EXCEPTION", "Request failed: " + e.getMessage());
        }
    }
}
