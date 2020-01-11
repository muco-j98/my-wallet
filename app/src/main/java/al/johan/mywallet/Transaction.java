package al.johan.mywallet;

import org.w3c.dom.Text;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaction_table")
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String description;

    private double amount;

    private String creationDate;

    public Transaction(String description, double amount, String creationDate) {
        this.description = description;
        this.amount = amount;
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCreationDate() {
        return creationDate;
    }
}
