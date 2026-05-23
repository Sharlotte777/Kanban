package firebase;

import android.util.Log;
import models.Ticket;
import com.google.firebase.database.*;
import java.util.HashMap;
import java.util.Map;

public class FirebaseSyncManager {
    private static final String TICKETS_PATH = "tickets";
    private DatabaseReference ticketsRef;
    private ValueEventListener ticketListener;

    public void startSync(String boardId, OnTicketChangeListener listener) {
        ticketsRef = FirebaseDatabase.getInstance().getReference(TICKETS_PATH);
        Query query = ticketsRef.orderByChild("boardId").equalTo(boardId);
        ticketListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Ticket> remoteTickets = new HashMap<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Ticket t = child.getValue(Ticket.class);
                    if (t != null) remoteTickets.put(child.getKey(), t);
                }
                if (listener != null) listener.onTicketsChanged(remoteTickets);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("FirebaseSync", error.getMessage());
            }
        };
        query.addValueEventListener(ticketListener);
    }

    public void stopSync() {
        if (ticketsRef != null && ticketListener != null) {
            ticketsRef.removeEventListener(ticketListener);
        }
    }

    public void pushTicket(Ticket ticket) {
        if (ticketsRef != null && ticket != null) {
            ticketsRef.child(ticket.getId()).setValue(ticket);
        }
    }

    public void deleteTicket(String ticketId) {
        if (ticketsRef != null && ticketId != null) {
            ticketsRef.child(ticketId).removeValue();
        }
    }

    public interface OnTicketChangeListener {
        void onTicketsChanged(Map<String, Ticket> tickets);
    }
}