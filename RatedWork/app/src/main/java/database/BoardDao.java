package database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import models.Board;
import java.util.List;

@Dao
public interface BoardDao {
    @Query("SELECT * FROM boards ORDER BY position")
    LiveData<List<Board>> getAllBoards();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Board board);

    @Update
    void update(Board board);

    @Delete
    void delete(Board board);
}
