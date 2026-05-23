package repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import database.AppDatabase;
import database.UserDao;
import models.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        userDao = AppDatabase.getInstance(context).userDao();
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void insert(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executor.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        executor.execute(() -> userDao.delete(user));
    }
}
