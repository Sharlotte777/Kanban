package ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import models.Board;
import models.Ticket;
import models.User;
import repository.BoardRepository;
import repository.TicketRepository;
import repository.UserRepository;
import utils.TelegramHelper;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private TicketRepository ticketRepository;
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    // Все тикеты текущей доски
    private LiveData<List<Ticket>> allTickets;
    // Отображаемые тикеты
    private MutableLiveData<List<Ticket>> displayTickets = new MutableLiveData<>();
    private String currentBoardId = "default";
    private boolean isSearchMode = false;
    private String lastQuery = "";

    public MainViewModel(Application app) {
        super(app);
        ticketRepository = new TicketRepository(app);
        boardRepository = new BoardRepository(app);
        userRepository = new UserRepository(app);
    }

    public void loadTickets(String boardId) {
        this.currentBoardId = boardId;
        allTickets = ticketRepository.getTicketsByBoard(boardId);
        // Подписываемся на изменения всех тикетов, но обновляем displayTickets только если не в поиске
        allTickets.observeForever(tickets -> {
            if (!isSearchMode) displayTickets.setValue(tickets);
        });
        if (!isSearchMode) displayTickets.setValue(allTickets.getValue());
    }

    public LiveData<List<Ticket>> searchTickets(String query) {
        String likeQuery = "%" + query + "%";
        return ticketRepository.searchTickets(likeQuery);
    }

    // Фильтрация по тексту
    public void filterTickets(String query) {
        if (query == null || query.trim().isEmpty()) {
            // Выход из поиска
            isSearchMode = false;
            lastQuery = "";
            displayTickets.setValue(allTickets.getValue());
        } else {
            isSearchMode = true;
            lastQuery = query;
            // Выполняем поиск в репозитории (возвращает LiveData)
            ticketRepository.searchTickets("%" + query + "%").observeForever(results -> {
                displayTickets.setValue(results);
            });
        }
    }

    // UI подписывается на LiveData
    public LiveData<List<Ticket>> getTickets() {
        return displayTickets;
    }

    public void insertTicket(Ticket ticket) {
        ticketRepository.insertTicket(ticket);
        if (isSearchMode) filterTickets(lastQuery);
    }

    public void updateTicket(Ticket ticket) {
        ticketRepository.updateTicket(ticket);
        if (isSearchMode) filterTickets(lastQuery);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.deleteTicket(ticket);
        if (isSearchMode) filterTickets(lastQuery);
    }

    public void duplicateTicket(Ticket ticket) {
        ticketRepository.duplicateTicket(ticket);
        if (isSearchMode) filterTickets(lastQuery);
    }

    public void refreshTickets(String boardId) {
        ticketRepository.stopSync();
        loadTickets(boardId);
    }

    public LiveData<List<Board>> getAllBoards() { return boardRepository.getAllBoards(); }
    public void insertBoard(Board board) { boardRepository.insert(board); }
    public void updateBoard(Board board) { boardRepository.update(board); }
    public void deleteBoard(Board board) { boardRepository.delete(board); }

    public LiveData<List<User>> getAllUsers() { return userRepository.getAllUsers(); }
    public void insertUser(User user) { userRepository.insert(user); }
    public void updateUser(User user) { userRepository.update(user); }
    public void deleteUser(User user) { userRepository.delete(user); }

    @Override
    protected void onCleared() {
        super.onCleared();
        ticketRepository.stopSync();
    }
}