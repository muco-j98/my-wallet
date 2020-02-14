package al.johan.mywallet.data.db.repositories;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import al.johan.mywallet.data.db.TransactionDao;
import al.johan.mywallet.data.db.TransactionDatabase;
import al.johan.mywallet.data.db.entities.Transaction;
import androidx.lifecycle.LiveData;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;

    public TransactionRepository(Application application) {
        TransactionDatabase database = TransactionDatabase.getInstance(application);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
    }

    public void insert(Transaction transaction) {
        new InsertTransactionAsyncTask(transactionDao).execute(transaction);
    }

    public void delete(Transaction transaction) {
        new DeleteTransactionAsyncTask(transactionDao).execute(transaction);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<Transaction>> getTransactionsByMonth(String month) {
        return transactionDao.getTransactionsByMonth(month);
    }

    public LiveData<List<Transaction>> getAllNegativeTransactionsByMonth(String month) {
        return transactionDao.getNegativeTransactionsByMonth(month);
    }

    private static class InsertTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {
        private TransactionDao transactionDao;

        private InsertTransactionAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            transactionDao.insert(transactions[0]);
            return null;
        }
    }

    private static class DeleteTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {
        private TransactionDao transactionDao;

        private DeleteTransactionAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            transactionDao.delete(transactions[0]);
            return null;
        }
    }
}
