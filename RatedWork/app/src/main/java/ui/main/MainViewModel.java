package ui.main;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import models.Board;
import models.Ticket;
import models.User;
import repository.BoardRepository;
import repository.TicketRepository;
import repository.UserRepository;
import utils.TelegramHelper;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private TicketRepository repository;
    private LiveData<List<Ticket>> tickets;
    private BoardRepository boardRepository;
    private UserRepository userRepository;
    private LiveData<List<Board>> boards;
    private LiveData<List<User>> users;

    public MainViewModel(Application app) {
        super(app);
        repository = new TicketRepository(app);
        boardRepository = new BoardRepository(app);
        userRepository = new UserRepository(app);
    }

    public void loadTickets(String boardId) {
        tickets = repository.getTicketsByBoard(boardId);
    }

    public LiveData<List<Ticket>> getTickets() {
        return tickets;
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

    public LiveData<List<Board>> getAllBoards() {
        if (boards == null) boards = boardRepository.getAllBoards();
        return boards;
    }

    public LiveData<List<User>> getAllUsers() {
        if (users == null) users = userRepository.getAllUsers();
        return users;
    }

    public void insertBoard(Board board) {
        boardRepository.insert(board);
    }

    public void updateBoard(Board board) {
        boardRepository.update(board);
    }

    public void deleteBoard(Board board) {
        boardRepository.delete(board);
    }

    public void refreshTickets(String boardId) {
        repository.stopSync();
        tickets = repository.getTicketsByBoard(boardId);
    }

    public void insertUser(User user) {
        userRepository.insert(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}