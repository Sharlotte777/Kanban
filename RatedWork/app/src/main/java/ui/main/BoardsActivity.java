package ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BoardsActivity extends AppCompatActivity {
    private RecyclerView boardsRecycler;
    private BoardAdapter adapter;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        boardsRecycler = findViewById(R.id.boardsRecycler);
        boardsRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoardAdapter();
        adapter.setViewModel(viewModel);
        adapter.setOnBoardMoveListener((from, to) -> {
        });
        boardsRecycler.setAdapter(adapter);

        // Для перетаскивания досок
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        });
        touchHelper.attachToRecyclerView(boardsRecycler);

        // Кнопка создания новой доски
        FloatingActionButton fabAddBoard = findViewById(R.id.fabAddBoard);
        fabAddBoard.setOnClickListener(v -> {
            startActivity(new Intent(this, BoardEditActivity.class));
        });

        viewModel.getAllBoards().observe(this, boards -> adapter.setBoards(boards));
    }
}