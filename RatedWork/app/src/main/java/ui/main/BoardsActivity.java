package ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratedwork.R;

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
        adapter = new BoardAdapter(board -> {
            Intent intent = new Intent();
            intent.putExtra("board_id", board.getId());
            setResult(RESULT_OK, intent);
            finish();
        });
        boardsRecycler.setAdapter(adapter);
    }
}