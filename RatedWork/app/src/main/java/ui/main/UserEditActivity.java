package ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.ratedwork.R;
import models.User;
import java.util.UUID;

public class UserEditActivity extends AppCompatActivity {
    private EditText nameEdit, telegramIdEdit;
    private Button saveButton;
    private MainViewModel viewModel;
    private User existingUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        nameEdit = findViewById(R.id.editUserName);
        telegramIdEdit = findViewById(R.id.editTelegramChatId);
        saveButton = findViewById(R.id.buttonSave);

        String userId = getIntent().getStringExtra("user_id");
        if (userId != null) {
            existingUser = (User) getIntent().getSerializableExtra("user");
            if (existingUser != null) {
                nameEdit.setText(existingUser.getName());
                telegramIdEdit.setText(existingUser.getTelegramChatId());
            }
        }

        saveButton.setOnClickListener(v -> saveUser());
    }

    private void saveUser() {
        String name = nameEdit.getText().toString().trim();
        if (name.isEmpty()) {
            nameEdit.setError("Required");
            return;
        }
        User user = existingUser != null ? existingUser : new User();
        if (existingUser == null) {
            user.setId(UUID.randomUUID().toString());
        }
        user.setName(name);
        user.setTelegramChatId(telegramIdEdit.getText().toString().trim());
        if (existingUser == null) {
            viewModel.insertUser(user);
        } else {
            viewModel.updateUser(user);
        }
        finish();
    }
}