package ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.ratedwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView columnsRecyclerView;
    private ColumnAdapter columnAdapter;
    private MainViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private static final String CURRENT_BOARD_ID = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        columnsRecyclerView = findViewById(R.id.columnsRecyclerView);
        columnsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Column> columns = Arrays.asList(
                new Column("todo", "To Do"),
                new Column("inprogress", "In Progress"),
                new Column("done", "Done")
        );
        columnAdapter = new ColumnAdapter(this, columns, viewModel);
        columnsRecyclerView.setAdapter(columnAdapter);

        // Поиск с фильтрацией в реальном времени
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // При нажатии Enter тоже фильтруем
                viewModel.filterTickets(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Фильтруем при изменении текста
                viewModel.filterTickets(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            viewModel.filterTickets("");  // Очистка фильтра
            return false;
        });

        // Для добавления тикета
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditTicketActivity.class);
            intent.putExtra("board_id", CURRENT_BOARD_ID);
            startActivity(intent);
        });

        Button btnBoards = findViewById(R.id.btnBoards);
        btnBoards.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, BoardsActivity.class)));
        Button btnUsers = findViewById(R.id.btnUsers);
        btnUsers.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UsersActivity.class)));
        Button btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            swipeRefreshLayout.setRefreshing(true);
            viewModel.refreshTickets(CURRENT_BOARD_ID);
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }, 1500);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.refreshTickets(CURRENT_BOARD_ID);
            swipeRefreshLayout.postDelayed(() -> {
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }, 1500);
        });

        viewModel.loadTickets(CURRENT_BOARD_ID);
    }
}