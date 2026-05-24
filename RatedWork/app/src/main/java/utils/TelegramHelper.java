package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;
import okhttp3.*;
import java.io.IOException;

public class TelegramHelper {
    private static final String TAG = "TelegramHelper";
    private static final String BOT_TOKEN = "8811953118:AAFw2Bl1OUSHSuMNPYdCDQOLjDgy0m-Hls4";

    public static void sendNotification(Context context, String assigneeId, String message) {
        Log.d(TAG, "sendNotification called, assigneeId=" + assigneeId);
        if (assigneeId == null || assigneeId.isEmpty()) {
            Log.w(TAG, "assigneeId is empty");
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String chatId = prefs.getString("telegram_chat_" + assigneeId, null);
        Log.d(TAG, "Retrieved chatId for " + assigneeId + " = " + chatId);
        if (chatId == null || chatId.isEmpty()) {
            Log.w(TAG, "No chatId found");
            return;
        }

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
        RequestBody body = new FormBody.Builder()
                .add("chat_id", chatId)
                .add("text", message)
                .add("parse_mode", "HTML")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Log.d(TAG, "Sending request to " + url + " with chatId=" + chatId);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Network failure", e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body() != null ? response.body().string() : "null";
                Log.d(TAG, "Response code: " + response.code() + ", body: " + body);
                response.close();
            }
        });
    }
}