package ui.main;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import models.Board;
import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private List<Board> boards = new ArrayList<>();
    private MainViewModel viewModel;
    private Handler clickHandler = new Handler();
    private int clickCount = 0;
    private Runnable clickRunnable;
    private OnBoardMoveListener moveListener;

    public interface OnBoardMoveListener {
        void onBoardMoved(int fromPosition, int toPosition);
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
        notifyDataSetChanged();
    }

    public void setViewModel(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setOnBoardMoveListener(OnBoardMoveListener listener) {
        this.moveListener = listener;
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Board moved = boards.remove(fromPosition);
        boards.add(toPosition, moved);
        notifyItemMoved(fromPosition, toPosition);
        if (moveListener != null) moveListener.onBoardMoved(fromPosition, toPosition);
    }

    @NonNull @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        Board board = boards.get(position);
        holder.name.setText(board.getName());

        holder.itemView.setOnClickListener(v -> {
            clickCount++;
            if (clickRunnable != null) clickHandler.removeCallbacks(clickRunnable);
            clickRunnable = () -> {
                if (clickCount == 1) {
                    Intent intent = new Intent(v.getContext(), BoardEditActivity.class);
                    intent.putExtra("board_id", board.getId());
                    intent.putExtra("board", board);
                    v.getContext().startActivity(intent);
                } else if (clickCount == 2) {
                    Board copy = new Board();
                    copy.setId(java.util.UUID.randomUUID().toString());
                    copy.setName(board.getName() + " (copy)");
                    copy.setPosition(board.getPosition() + 1);
                    viewModel.insertBoard(copy);
                } else if (clickCount >= 3) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Delete board")
                            .setMessage("Delete \"" + board.getName() + "\"?")
                            .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteBoard(board))
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                clickCount = 0;
                clickRunnable = null;
            };
            clickHandler.postDelayed(clickRunnable, 300);
        });
    }

    @Override
    public int getItemCount() { return boards.size(); }

    static class BoardViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.boardName);
        }
    }
}