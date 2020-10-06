package co.edu.unal.clockrow.data.Task;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;
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
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TaskDao dao = INSTANCE.taskDao();
                dao.deleteAll();
                Task task = new Task("Una tarea urgente", "Esta tarea es urgente y a parte también es difícil.",
                        Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
                        5f, "Urgente"); dao.insert(task);
                task = new Task("Una tarea normal", "Esta tarea es normal, poca dificultad y poca prioridad.",
                        Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
                        3.5f, "Baja"); dao.insert(task);
            });
        }
    };
}
