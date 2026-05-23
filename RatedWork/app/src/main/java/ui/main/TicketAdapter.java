package ui.main;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import models.Ticket;
import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private List<Ticket> tickets = new ArrayList<>();
    private MainViewModel viewModel;

    public TicketAdapter(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    public Ticket getTicketAt(int position) {
        return tickets.get(position);
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

        // Устанавливаем долгое нажатие для запуска drag
        holder.cardView.setOnLongClickListener(v -> {
            // Создаём ClipData с идентификатором тикета (или целым объектом)
            ClipData.Item item = new ClipData.Item(ticket.getId());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(ticket.getId(), mimeTypes, item);

            // Создаём ShadowBuilder для кастомной тени
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

            // Запускаем drag
            v.startDrag(data, shadowBuilder, ticket, 0);
            return true;
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
