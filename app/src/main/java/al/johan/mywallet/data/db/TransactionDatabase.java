package al.johan.mywallet.data.db;

import android.content.Context;

import al.johan.mywallet.data.db.entities.Transaction;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Transaction.class, version = 4, exportSchema = false)
public abstract class TransactionDatabase extends RoomDatabase {

    private static TransactionDatabase instance;

    public abstract TransactionDao transactionDao();

    public static synchronized TransactionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TransactionDatabase.class, "transaction_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
