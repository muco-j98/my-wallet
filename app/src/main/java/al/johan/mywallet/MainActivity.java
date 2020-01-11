package al.johan.mywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CreateTransactionDialog.CreateTransactionDialogListener {
    private TransactionViewModel transactionViewModel;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tVTotalAmount = findViewById(R.id.tvTotalAmount);

        FloatingActionButton btnAddTransaction = findViewById(R.id.btnAddTransaction);
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.transaction_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TransactionAdapter adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.getAllTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);

                for(int i = 0; i < transactions.size(); i++) {
                    totalAmount += transactions.get(i).getAmount();
                }
                tVTotalAmount.setText(String.valueOf(totalAmount));
            }
        });
    }

    private void openDialog() {
        CreateTransactionDialog createTransactionDialog = new CreateTransactionDialog();
        createTransactionDialog.show(getSupportFragmentManager(), "create fragment dialog");
    }

    @Override
    public void applyCreation(String description, double amount) {
        DateFormat sortable = new SimpleDateFormat("dd-MM");
        Date now = Calendar.getInstance().getTime();
        String timestampish = sortable.format(now);

        Transaction transaction = new Transaction(description, amount, timestampish);
        transactionViewModel.insert(transaction);

        Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show();
    }
}
