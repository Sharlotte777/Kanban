package database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import models.Ticket;
import models.Board;

@Database(entities = {Ticket.class, Board.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketDao ticketDao();
    public abstract BoardDao boardDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kanban.db")
                            .build();
                }
            }
        }
        return instance;
    }
}