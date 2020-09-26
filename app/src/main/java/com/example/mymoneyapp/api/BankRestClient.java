package com.example.mymoneyapp.api;

import android.util.Pair;

import com.example.mymoneyapp.common.Constants;
import com.example.mymoneyapp.data.model.BankAccount;
import com.example.mymoneyapp.data.model.UserCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.util.HttpConstants;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.example.mymoneyapp.common.Constants.HOST_BASE_URL;

public class BankRestClient {
    private static AsyncHttpClient client;
    private static Gson GSON;
    private static String baseUrl;

    private BankRestClient() {
        client = Dsl.asyncHttpClient();
        GSON = new Gson();
        baseUrl = HOST_BASE_URL;
    }

    public static Pair<String, BankAccount> getUserDataForCredentials(UserCredentials userCredentials) {
        establishConnectionIfNeeded();

        Request req = getRequest(HttpConstants.Methods.GET,"login", GSON.toJson(userCredentials));

        try {
            Response res = executeRequest(req);
            JsonObject responseBodyAsJson = responseBodyAsJson(res);
            String serverStringResponse = getResponseStringFromServer(responseBodyAsJson);
            if (!isServerResponseIsSuccessfull(serverStringResponse)){
                resetClientConnection();
                return new Pair<>(serverStringResponse, null);
            }
            else
                return new Pair<>(serverStringResponse, getBankAccountFromJson(responseBodyAsJson));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void logout() {
        establishConnectionIfNeeded();
        try {
            Request logoutRequest = getRequest(HttpConstants.Methods.POST, "logout");
            executeRequest(logoutRequest);
            resetClientConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deposit(int accountNumber, float amount) {
        establishConnectionIfNeeded();
        Request depositRequest = getRequest(HttpConstants.Methods.POST,
                "deposit/" + accountNumber + "/" + amount);

        executeRequest(depositRequest);

        return true;
    }

    public static String withdraw(int accountNumber, float amount) {
        establishConnectionIfNeeded();

        Request withDrawRequest = getRequest(HttpConstants.Methods.POST,
                "withdraw/" + accountNumber + "/" + amount);

        Response res = executeRequest(withDrawRequest);;

        return getTransactionStatusFromResponse(res);
    }

    public static Number getUsersCurrentBalance(int accountNumber) {
        establishConnectionIfNeeded();

        Request accountsCurrentBalanceRequest = getRequest(HttpConstants.Methods.GET,
                "bank/balance/" + accountNumber);

        Response res = executeRequest(accountsCurrentBalanceRequest);
        return GSON.fromJson(res.getResponseBody(), float.class);
    }

    public static String executeTransactionToAnotherAccount(int sourceAccountNumber,
                                                            float amount,
                                                            int destinationAccountNumber) {
        establishConnectionIfNeeded();
        Request transactionRequest = getRequest(HttpConstants.Methods.POST,
                "transaction/" + sourceAccountNumber + "/" + amount + "/" + destinationAccountNumber);

        Response res = executeRequest(transactionRequest);
        return getTransactionStatusFromResponse(res);
    }

    private static Request getRequest(String method, String extendedUrl, String body) {
        Request req = new RequestBuilder(method)
                .setUrl(baseUrl + extendedUrl + "/")
                .setBody(body)
                .build();
        req.getHeaders().add("Content-Type", "application/json");
        return req;
    }

    private static Request getRequest(String method, String extendedUrl) {
        Request req = new RequestBuilder(method)
                .setUrl(baseUrl + extendedUrl + "/")
                .build();
        req.getHeaders().add("Content-Type", "application/json");
        return req;
    }

    private static Response executeRequest(Request req){
        ListenableFuture<Response> responseFuture = client.executeRequest(req);
        Response res = null;
        try {
            res = responseFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static JsonObject responseBodyAsJson(Response res){
        return GSON.fromJson(res.getResponseBody(), JsonObject.class);
    }

    private static BankAccount getBankAccountFromJson(JsonObject body){
        return GSON.fromJson(body.get("entity"), BankAccount.class);
    }

    private static String getResponseStringFromServer(JsonObject responseAsJson){
        return responseAsJson.get("headers").getAsJsonObject().get("login").getAsString();
    }

    private static boolean isServerResponseIsSuccessfull(String serverStringResponse) {
        return serverStringResponse.equals(Constants.SUCCESSFUL);
    }

    private static void establishConnectionIfNeeded() {
        if (client == null)
            new BankRestClient();
    }

    private static void resetClientConnection() throws IOException {
        client.close();
        client = null;
    }

    private static String getTransactionStatusFromResponse(Response res){
        JsonObject responseBodyAsJson = responseBodyAsJson(res);
        return responseBodyAsJson.get("headers").getAsJsonObject().get("TransactionStatus").getAsString();
    }
}
