package repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import database.AppDatabase;
import database.TicketDao;
import models.Ticket;
import firebase.FirebaseSyncManager;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketRepository {
    private TicketDao ticketDao;
    private FirebaseSyncManager syncManager;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public TicketRepository(Context context) {
        ticketDao = AppDatabase.getInstance(context).ticketDao();
        syncManager = new FirebaseSyncManager();
    }

    public LiveData<List<Ticket>> getTicketsByBoard(String boardId) {
        syncManager.startSync(boardId, remoteMap -> {
            executor.execute(() -> {
                for (Ticket t : remoteMap.values()) {
                    ticketDao.insert(t);
                }
            });
        });
        return ticketDao.getTicketsByBoard(boardId);
    }

    // Поиск
    public LiveData<List<Ticket>> searchTickets(String query) {
        return ticketDao.searchTickets(query); // query уже содержит %%
    }

    public void insertTicket(Ticket ticket) {
        executor.execute(() -> {
            ticketDao.insert(ticket);
            syncManager.pushTicket(ticket);
        });
    }

    public void updateTicket(Ticket ticket) {
        executor.execute(() -> {
            ticketDao.update(ticket);
            syncManager.pushTicket(ticket);
        });
    }

    public void deleteTicket(Ticket ticket) {
        executor.execute(() -> {
            ticketDao.delete(ticket);
            syncManager.deleteTicket(ticket.getId());
        });
    }

    public void duplicateTicket(Ticket original) {
        executor.execute(() -> {
            Ticket copy = new Ticket();
            copy.setId(UUID.randomUUID().toString());
            copy.setTitle(original.getTitle() + " (copy)");
            copy.setDescription(original.getDescription());
            copy.setTags(original.getTags());
            copy.setAssigneeId(original.getAssigneeId());
            copy.setBoardId(original.getBoardId());
            copy.setExternalLink(original.getExternalLink());
            copy.setAttachments(original.getAttachments());
            copy.setStatus(original.getStatus());
            copy.setPosition(original.getPosition() + 1);
            copy.setCreatedAt(System.currentTimeMillis());
            copy.setUpdatedAt(System.currentTimeMillis());
            ticketDao.insert(copy);
            syncManager.pushTicket(copy);
        });
    }

    public void stopSync() {
        syncManager.stopSync();
    }
}