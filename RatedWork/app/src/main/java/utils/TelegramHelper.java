package utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import okhttp3.*;
import java.io.IOException;

public class TelegramHelper {
    private static final String BOT_TOKEN = "8811953118:AAFw2Bl1OUSHSuMNPYdCDQOLjDgy0m-Hls4";

    public static void sendNotification(Context context, String assigneeId, String message) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String chatId = prefs.getString("telegram_chat_" + assigneeId, null);
        if (chatId == null) return;

        OkHttpClient client = new OkHttpClient();
        String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
        RequestBody body = new FormBody.Builder()
                .add("chat_id", chatId)
                .add("text", message)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) { e.printStackTrace(); }
            @Override public void onResponse(Call call, Response response) throws IOException { }
        });
    }
}
