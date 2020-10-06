package co.edu.unal.clockrow.data.Task;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.edu.unal.clockrow.logic.Task;

@Database(entities ={Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDB extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile TaskRoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TaskRoomDB getDataBase(final Context context){

        if(INSTANCE == null){
            synchronized (TaskRoomDB.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDB.class,
                            "tasks")
                            .addCallback(sRoomDataBaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDataBaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TaskDao dao = INSTANCE.taskDao();
                dao.deleteAll();

                Task task = new Task("Hello", "It's me",  Calendar.getInstance().getTime());
                dao.insert(task);
                task = new Task("World", "Zaaaaaaaaaa", Calendar.getInstance().getTime());
                dao.insert(task);
            });
        }
    };
}
