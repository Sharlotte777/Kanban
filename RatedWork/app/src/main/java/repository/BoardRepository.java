package repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import database.AppDatabase;
import database.BoardDao;
import models.Board;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BoardRepository {
    private BoardDao boardDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public BoardRepository(Context context) {
        boardDao = AppDatabase.getInstance(context).boardDao();
    }

    public LiveData<List<Board>> getAllBoards() {
        return boardDao.getAllBoards();
    }

    public void insert(Board board) {
        executor.execute(() -> boardDao.insert(board));
    }

    public void update(Board board) {
        executor.execute(() -> boardDao.update(board));
    }

    public void delete(Board board) {
        executor.execute(() -> boardDao.delete(board));
    }
}
