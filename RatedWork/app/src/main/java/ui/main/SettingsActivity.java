package ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.example.ratedwork.R;

public class SettingsActivity extends AppCompatActivity {
    private EditText userIdEdit, chatIdEdit;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userIdEdit = findViewById(R.id.userIdEdit);
        chatIdEdit = findViewById(R.id.telegramChatId);
        saveButton = findViewById(R.id.saveChatIdButton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Загружаем сохранённые значения
        String savedUserId = prefs.getString("last_user_id", "");
        String savedChatId = prefs.getString("telegram_chat_" + savedUserId, "");
        userIdEdit.setText(savedUserId);
        chatIdEdit.setText(savedChatId);

        saveButton.setOnClickListener(v -> {
            String userId = userIdEdit.getText().toString().trim();
            String chatId = chatIdEdit.getText().toString().trim();

            if (userId.isEmpty()) {
                userIdEdit.setError("User ID required");
                return;
            }
            if (chatId.isEmpty()) {
                chatIdEdit.setError("Chat ID required");
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("telegram_chat_" + userId, chatId);
            editor.putString("last_user_id", userId);
            editor.apply();

            Toast.makeText(SettingsActivity.this, "Saved! Notifications will be sent to this chat.", Toast.LENGTH_LONG).show();
        });
    }
}