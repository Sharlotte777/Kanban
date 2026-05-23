package ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ratedwork.R;

import models.Board;
import java.util.ArrayList;
import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {
    private List<Board> boards = new ArrayList<>();
    private OnBoardClickListener listener;

    public BoardAdapter(OnBoardClickListener listener) {
        this.listener = listener;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
        notifyDataSetChanged();
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
        holder.itemView.setOnClickListener(v -> listener.onClick(board));
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

    public interface OnBoardClickListener {
        void onClick(Board board);
    }
}