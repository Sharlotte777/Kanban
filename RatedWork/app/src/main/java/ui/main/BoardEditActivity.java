package ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.ratedwork.R;
import models.Board;
import java.util.UUID;

public class BoardEditActivity extends AppCompatActivity {
    private EditText nameEdit;
    private Button saveButton;
    private MainViewModel viewModel;
    private Board existingBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        nameEdit = findViewById(R.id.editBoardName);
        saveButton = findViewById(R.id.buttonSave);

        String boardId = getIntent().getStringExtra("board_id");
        if (boardId != null) {
            existingBoard = (Board) getIntent().getSerializableExtra("board");
            if (existingBoard != null) {
                nameEdit.setText(existingBoard.getName());
            }
        }

        saveButton.setOnClickListener(v -> saveBoard());
    }

    private void saveBoard() {
        String name = nameEdit.getText().toString().trim();
        if (name.isEmpty()) {
            nameEdit.setError("Required");
            return;
        }
        Board board = existingBoard != null ? existingBoard : new Board();
        if (existingBoard == null) {
            board.setId(UUID.randomUUID().toString());
        }
        board.setName(name);
        if (existingBoard == null) {
            board.setPosition(Integer.MAX_VALUE);
            viewModel.insertBoard(board);
        } else {
            viewModel.updateBoard(board);
        }
        finish();
    }
}
