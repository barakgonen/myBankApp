package com.example.mymoneyapp.api;

import com.example.mymoneyapp.data.model.BankAccount;
import com.example.mymoneyapp.data.model.UserCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import java.util.concurrent.ExecutionException;

public class BankRestClient {
    private static AsyncHttpClient client;
    private static Gson GSON;
    private static String baseUrl;

    private BankRestClient() {
        client = Dsl.asyncHttpClient();
        GSON = new Gson();
        baseUrl = "http://10.0.2.2:8080/api/";
    }

    public static BankAccount getUserDataForCredentials(UserCredentials userCredentials) {
        if (client == null)
            new BankRestClient();
        Request s = new RequestBuilder(HttpConstants.Methods.GET)
                .setBody(GSON.toJson(userCredentials))
                .setUrl(baseUrl + "account/")
                .build();

        s.getHeaders().add("Content-Type", "application/json");
        ListenableFuture<Response> responseFuture = client.executeRequest(s);
        try {
            return GSON.fromJson(responseFuture.get().getResponseBody(), BankAccount.class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void logout(Integer accountNumber){

    }
//
//    public static boolean deleteNote(Long id) {
//        Request deleteRequest = Dsl.delete(baseUrl + "delete/" + id).build();
//        ListenableFuture<Response> responseFuture = client.executeRequest(deleteRequest);
//        try {
//            responseFuture.get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return true;
//    }
//
    public static boolean deposit(int accountNumber, float amount) {
        if (client == null)
            new BankRestClient();
        Request s = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl(baseUrl + "deposit/" + accountNumber + "/" + amount)
                .build();
        s.getHeaders().add("Content-Type", "application/json");

        ListenableFuture<Response> responseFuture = client.executeRequest(s);
        return true;
    }

    public static boolean withdraw(int accountNumber, float amount) {
        if (client == null)
            new BankRestClient();
        Request s = new RequestBuilder(HttpConstants.Methods.POST)
                .setUrl(baseUrl + "withdraw/" + accountNumber + "/" + amount)
                .build();
        s.getHeaders().add("Content-Type", "application/json");

        ListenableFuture<Response> responseFuture = client.executeRequest(s);
        return true;
    }

    public static Number getUsersCurrentBalance(int accountNumber){
        if (client == null)
            new BankRestClient();
        Request s = new RequestBuilder(HttpConstants.Methods.GET)
                .setUrl(baseUrl + "bank/balance/" + accountNumber)
                .build();

        s.getHeaders().add("Content-Type", "application/json");
        ListenableFuture<Response> responseFuture = client.executeRequest(s);
        try {
            return GSON.fromJson(responseFuture.get().getResponseBody(), float.class);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
