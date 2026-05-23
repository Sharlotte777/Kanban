package ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ratedwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private RecyclerView columnsRecyclerView;
    private ColumnAdapter columnAdapter;
    private MainViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String CURRENT_BOARD_ID = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        columnsRecyclerView = findViewById(R.id.columnsRecyclerView);
        columnsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Колонки
        java.util.List<Column> columns = java.util.Arrays.asList(
                new Column("todo", "To Do"),
                new Column("inprogress", "In Progress"),
                new Column("done", "Done")
        );
        columnAdapter = new ColumnAdapter(this, columns, viewModel);
        columnsRecyclerView.setAdapter(columnAdapter);

        // Для добавления тикета
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditTicketActivity.class);
            intent.putExtra("board_id", CURRENT_BOARD_ID);
            startActivity(intent);
        });

        // Кнопка "Доски"
        Button btnBoards = findViewById(R.id.btnBoards);
        btnBoards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BoardsActivity.class)));

        // Кнопка "Пользователи"
        Button btnUsers = findViewById(R.id.btnUsers);
        btnUsers.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsersActivity.class)));

        // Кнопка "Обновить"
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            swipeRefreshLayout.setRefreshing(true);
            viewModel.refreshTickets(CURRENT_BOARD_ID);
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1500);
        });

        // Свайп для обновления
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshTickets(CURRENT_BOARD_ID);
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 1500);
        });

        viewModel.loadTickets(CURRENT_BOARD_ID);
    }

    // Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}