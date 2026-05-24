package ui.main;

import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ratedwork.R;
import models.Ticket;
import java.util.ArrayList;
import java.util.List;

public class ColumnAdapter extends RecyclerView.Adapter<ColumnAdapter.ColumnViewHolder> {
    private List<Column> columns;
    private MainViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public ColumnAdapter(LifecycleOwner lifecycleOwner, List<Column> columns, MainViewModel viewModel) {
        this.lifecycleOwner = lifecycleOwner;
        this.columns = columns;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_column, parent, false);
        return new ColumnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColumnViewHolder holder, int position) {
        Column column = columns.get(position);
        holder.columnTitle.setText(column.getTitle());

        TicketAdapter ticketAdapter = new TicketAdapter(viewModel);
        holder.ticketsRecycler.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.ticketsRecycler.setAdapter(ticketAdapter);

        // Наблюдаем за displayTickets
        viewModel.getTickets().observe(lifecycleOwner, tickets -> {
            if (tickets == null) tickets = new ArrayList<>();
            List<Ticket> filtered = new ArrayList<>();
            for (Ticket t : tickets) {
                if (column.getStatus().equals(t.getStatus())) {
                    filtered.add(t);
                }
            }
            ticketAdapter.setTickets(filtered);
        });

        // Drag-and-drop
        holder.itemView.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                case DragEvent.ACTION_DROP:
                    Ticket draggedTicket = (Ticket) event.getLocalState();
                    if (draggedTicket != null && !column.getStatus().equals(draggedTicket.getStatus())) {
                        draggedTicket.setStatus(column.getStatus());
                        draggedTicket.setPosition(Integer.MAX_VALUE);
                        viewModel.updateTicket(draggedTicket);
                    }
                    return true;
                default:
                    return true;
            }
        });
    }

    @Override
    public int getItemCount() { return columns.size(); }

    static class ColumnViewHolder extends RecyclerView.ViewHolder {
        TextView columnTitle;
        RecyclerView ticketsRecycler;
        public ColumnViewHolder(@NonNull View itemView) {
            super(itemView);
            columnTitle = itemView.findViewById(R.id.columnTitle);
            ticketsRecycler = itemView.findViewById(R.id.ticketsRecycler);
        }
    }
}