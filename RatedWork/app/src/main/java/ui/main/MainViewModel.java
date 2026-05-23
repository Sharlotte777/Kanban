package ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import models.Ticket;
import repository.TicketRepository;
import utils.TelegramHelper;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private TicketRepository repository;
    private LiveData<List<Ticket>> tickets;

    public MainViewModel(Application app) {
        super(app);
        repository = new TicketRepository(app);
    }

    public void loadTickets(String boardId) {
        tickets = repository.getTicketsByBoard(boardId);
    }

    public LiveData<List<Ticket>> getTickets() {
        return tickets;
    }

    public void moveTicket(Ticket ticket, String newStatus, int newPosition) {
        ticket.setStatus(newStatus);
        ticket.setPosition(newPosition);
        repository.updateTicket(ticket);
        if (ticket.getAssigneeId() != null && !ticket.getAssigneeId().isEmpty()) {
            String msg = "Ticket \"" + ticket.getTitle() + "\" moved to " + newStatus;
            TelegramHelper.sendNotification(getApplication(), ticket.getAssigneeId(), msg);
        }
    }

    public void updateTicket(Ticket ticket) {
        repository.updateTicket(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        repository.deleteTicket(ticket);
    }

    public void insertTicket(Ticket ticket) {
        repository.insertTicket(ticket);
    }

    public void duplicateTicket(Ticket ticket) {
        repository.duplicateTicket(ticket);
    }

    public LiveData<List<Ticket>> searchTickets(String query) {
        return repository.searchTickets(query);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.stopSync();
    }
}