package al.johan.mywallet.ui.transaction;

import android.app.Application;

import java.util.List;

import al.johan.mywallet.data.db.entities.Transaction;
import al.johan.mywallet.data.db.repositories.TransactionRepository;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepository repository;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
        LiveData<List<Transaction>> allTransactions = repository.getAllTransactions();
    }

    public void insert(Transaction transaction) {
        repository.insert(transaction);
    }

    public void delete(Transaction transaction) {
        repository.delete(transaction);
    }

    public LiveData<List<Transaction>> getAllNegativeTransactionsByMonth(String month) {
        return repository.getAllNegativeTransactionsByMonth(month);
    }

    public LiveData<List<Transaction>> getTransactionsByMonth(String month) {
        return repository.getTransactionsByMonth(month);
    }
}
