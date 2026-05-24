package ui.main;

import android.content.ClipData;
import android.content.ClipDescription;
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
import models.Ticket;
import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> tickets = new ArrayList<>();
    private MainViewModel viewModel;
    private Handler clickHandler = new Handler();
    private int clickCount = 0;
    private Runnable clickRunnable;

    public TicketAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.title.setText(ticket.getTitle());
        holder.assignee.setText(ticket.getAssigneeId() != null ? ticket.getAssigneeId() : "Unassigned");

        // Drag-and-drop
        holder.cardView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item(ticket.getId());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(ticket.getId(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, ticket, 0);
            return true;
        });

        // Обработка кликов: одинарный - редактирование, двойной - дублирование, тройной - удаление
        holder.cardView.setOnClickListener(v -> {
            clickCount++;
            if (clickRunnable != null) {
                clickHandler.removeCallbacks(clickRunnable);
            }
            clickRunnable = () -> {
                if (clickCount == 1) {
                    // Открыть редактирование
                    Intent intent = new Intent(v.getContext(), EditTicketActivity.class);
                    intent.putExtra("ticket_id", ticket.getId());
                    intent.putExtra("ticket", ticket); // передаём объект (должен быть Serializable)
                    intent.putExtra("board_id", ticket.getBoardId());
                    v.getContext().startActivity(intent);
                } else if (clickCount == 2) {
                    // Дублирование
                    viewModel.duplicateTicket(ticket);
                } else if (clickCount >= 3) {
                    // Удаление с подтверждением
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Delete ticket")
                            .setMessage("Are you sure you want to delete \"" + ticket.getTitle() + "\"?")
                            .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteTicket(ticket))
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
    public int getItemCount() {
        return tickets.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView title, assignee;
        View cardView;
        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.ticketTitle);
            assignee = itemView.findViewById(R.id.ticketAssignee);
        }
    }
}