package com.example.mymoneyapp.data;

import com.example.mymoneyapp.data.model.BankAccount;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    protected String responseFromServer;
    private Result() {
    }

    @Override
    public String toString() {
        if (this instanceof Result.Success) {
            Result.Success success = (Result.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error) {
            Result.Error error = (Result.Error) this;
            return "Error[response from server =" + error.responseFromServer + "]";
        }
        return "";
    }

    public String getResponseFromServer(){
        return responseFromServer;
    }

    // Success sub-class
    public final static class Success<T> extends Result {
        private T data;

        public Success(T data, String serverResponseString) {
            this.data = data;
            this.responseFromServer = serverResponseString;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        public Error(String errorMsg) {
            responseFromServer = errorMsg;
        }
    }
}