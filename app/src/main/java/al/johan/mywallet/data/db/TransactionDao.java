package al.johan.mywallet.data.db;

import java.util.List;

import al.johan.mywallet.data.db.entities.Transaction;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transaction_table ORDER BY id DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transaction_table WHERE amount < 0 AND strftime('%m', creationDate) = :month")
    LiveData<List<Transaction>> getNegativeTransactionsByMonth(String month);

    @Query("SELECT * FROM transaction_table WHERE strftime('%m', creationDate) = :month ORDER BY id DESC")
    LiveData<List<Transaction>> getTransactionsByMonth(String month);
}
