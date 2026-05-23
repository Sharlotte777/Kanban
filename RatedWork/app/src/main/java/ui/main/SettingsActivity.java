package ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ratedwork.R;

public class SettingsActivity extends AppCompatActivity {
    private EditText chatIdEdit;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        chatIdEdit = findViewById(R.id.telegramChatId);
        saveButton = findViewById(R.id.saveChatIdButton);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String savedChatId = prefs.getString("telegram_chat_current_user", "");
        chatIdEdit.setText(savedChatId);

        saveButton.setOnClickListener(v -> {
            String chatId = chatIdEdit.getText().toString().trim();
            prefs.edit().putString("telegram_chat_current_user", chatId).apply();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        });
    }
}