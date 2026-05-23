package repository;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import models.Ticket;

public class MainViewModel extends AndroidViewModel {
    private TicketRepository repo;
    private LiveData<List<Ticket>> tickets;

    public MainViewModel(Application app) {
        super(app);
        repo = new TicketRepository(app);
    }
}