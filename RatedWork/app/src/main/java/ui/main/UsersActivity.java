package ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView usersRecycler;
    private UserAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        usersRecycler = findViewById(R.id.usersRecycler);
        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        adapter.setViewModel(viewModel);
        usersRecycler.setAdapter(adapter);

        // Кнопка создания нового пользователя
        FloatingActionButton fabAddUser = findViewById(R.id.fabAddUser);
        fabAddUser.setOnClickListener(v -> {
            startActivity(new Intent(this, UserEditActivity.class));
        });

        viewModel.getAllUsers().observe(this, users -> adapter.setUsers(users));
    }
}