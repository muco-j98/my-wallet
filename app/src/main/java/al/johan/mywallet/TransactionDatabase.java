package al.johan.mywallet;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Transaction.class, version = 3)
public abstract class TransactionDatabase extends RoomDatabase {

    private static TransactionDatabase instance;

    public abstract TransactionDao transactionDao();

    public static synchronized TransactionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TransactionDatabase.class, "transaction_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TransactionDao mTransactionDao;

        private PopulateDbAsyncTask(TransactionDatabase db) {
            mTransactionDao = db.transactionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTransactionDao.insert(new Transaction("Buke ne Stop&Shop", -7));
            mTransactionDao.insert(new Transaction("Tips nga xhamat", 25));
            return null;
        }
    }
}
