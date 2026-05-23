package ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView columnsRecyclerView;
    private ColumnAdapter columnAdapter;
    private MainViewModel viewModel;
    private String currentBoardId = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        columnsRecyclerView = findViewById(R.id.columnsRecyclerView);
        columnsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<Column> columns = Arrays.asList(
                new Column("todo", "To Do"),
                new Column("inprogress", "In Progress"),
                new Column("done", "Done")
        );
        columnAdapter = new ColumnAdapter(this, columns, viewModel);
        columnsRecyclerView.setAdapter(columnAdapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditTicketActivity.class);
            intent.putExtra("board_id", currentBoardId);
            startActivity(intent);
        });

        viewModel.loadTickets(currentBoardId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            currentBoardId = data.getStringExtra("board_id");
            viewModel.loadTickets(currentBoardId);
        }
    }
}
