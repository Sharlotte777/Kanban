package database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import models.Ticket;
import models.Board;
import models.User;

@Database(entities = {Ticket.class, Board.class, User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();
    public abstract BoardDao boardDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kanban.db")
                            .fallbackToDestructiveMigration() // Пересоздаёт базу данных
                            .build();
                }
            }
        }
        return instance;
    }
}