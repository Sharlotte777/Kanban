package database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import models.Ticket;
import java.util.List;

@Dao
public interface TicketDao {
    @Query("SELECT * FROM tickets WHERE boardId = :boardId ORDER BY position")
    LiveData<List<Ticket>> getTicketsByBoard(String boardId);

    @Query("SELECT * FROM tickets WHERE title LIKE :query OR description LIKE :query OR tags LIKE :query")
    LiveData<List<Ticket>> searchTickets(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Delete
    void delete(Ticket ticket);
}
