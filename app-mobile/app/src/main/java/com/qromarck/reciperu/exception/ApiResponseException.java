package com.qromarck.reciperu.exception;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


    public class ApiResponseException extends RuntimeException{
        private int code;
        private String message;

        public ApiResponseException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Nullable
        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @NonNull
        @Override
        public String toString() {
            return "ApiResponseException{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

