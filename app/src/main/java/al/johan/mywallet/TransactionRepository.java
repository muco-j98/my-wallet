package al.johan.mywallet;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TransactionRepository {

    private TransactionDao transactionDao;
    private LiveData<List<Transaction>> allTransactions;
    private LiveData<List<Transaction>> allNegativeTransactionsWithinDate;
    private String date;

    public TransactionRepository(Application application) {
        TransactionDatabase database = TransactionDatabase.getInstance(application);
        transactionDao = database.transactionDao();
        allTransactions = transactionDao.getAllTransactions();
        allNegativeTransactionsWithinDate = transactionDao.getNegativeTransactionsWithinDate();
    }

    public void insert(Transaction transaction) {
        new InsertTransactionAsyncTask(transactionDao).execute(transaction);
    }

    public void update(Transaction transaction) {
        new UpdateTransactionAsyncTask(transactionDao).execute(transaction);
    }

    public void delete(Transaction transaction) {
        new DeleteTransactionAsyncTask(transactionDao).execute(transaction);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return allTransactions;
    }

    public LiveData<List<Transaction>> getAllNegativeTransactionsWithinDate() {
        return allNegativeTransactionsWithinDate;
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

    private static class UpdateTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {
        private TransactionDao transactionDao;

        private UpdateTransactionAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            transactionDao.update(transactions[0]);
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
