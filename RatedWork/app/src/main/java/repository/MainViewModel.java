package repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import models.Ticket;
import utils.TelegramHelper;

public class MainViewModel extends AndroidViewModel {
    private TicketRepository repo;
    private LiveData<List<Ticket>> tickets;

    public MainViewModel(Application app) {
        super(app);
        repo = new TicketRepository(app);
    }

    public void loadTickets(String boardId) {
        tickets = repo.getTicketsByBoard(boardId);
    }

    public LiveData<List<Ticket>> getTickets() { return tickets; }

    public void moveTicket(Ticket ticket, String newStatus, int newPos) {
        ticket.setStatus(newStatus);
        ticket.setPosition(newPos);
        repo.updateTicket(ticket);
        // уведомление в Telegram при смене статуса (см. отдельно)
        notifyTelegram(ticket, "Status changed to " + newStatus);
    }

    private void notifyTelegram(Ticket ticket, String message) {
        // вызов асинхронного метода
        TelegramHelper.sendNotification(getApplication(), ticket.getAssigneeId(), message);
    }
}