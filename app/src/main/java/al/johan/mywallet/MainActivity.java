package al.johan.mywallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
    private TextView tvTotalAmount;
    private EditText etInitialAmount;
    double totalAmount, initialAmount;
    private final int REQUEST_CODE = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INITIAL_AMOUNT = "initialAmount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean firstStart = prefs.getBoolean("firstStart", true);

                if (firstStart) {
                    new openActivityAsync(getApplicationContext()).execute();
                }
            }
        }).start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        boolean firstStart = prefs.getBoolean("firstStart", true);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

//        if (firstStart) {
//            showInitialActivity();
//            etInitialAmount = findViewById(R.id.etInitialAmount);
//        }

        loadData();

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
                tvTotalAmount.setText(String.valueOf(calculateTotal(transactions)));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Transaction currentTransaction = adapter.getTransactionAt(viewHolder.getAdapterPosition());
                transactionViewModel.delete(currentTransaction);
                tvTotalAmount.setText(String.valueOf(updateTotalAfterDelete(currentTransaction)));
                Toast.makeText(MainActivity.this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                initialAmount = data.getDoubleExtra("initialAmount", 0);
                Toast.makeText(this, "value is + " + initialAmount, Toast.LENGTH_SHORT).show();
                updateViews();
            }
        }
    }

    private void openDialog() {
        CreateTransactionDialog createTransactionDialog = new CreateTransactionDialog();
        createTransactionDialog.show(getSupportFragmentManager(), "create fragment dialog");
    }

    @Override
    public void applyCreation(String description, double amount, String category) {
        DateFormat sortable = new SimpleDateFormat("dd-MM");
        Date now = Calendar.getInstance().getTime();
        String timestampish = sortable.format(now);

        Transaction transaction = new Transaction(description, amount, timestampish, category);
        transactionViewModel.insert(transaction);

        Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show();
    }

    public double calculateTotal(List<Transaction> transactions) {
        totalAmount = initialAmount;
        for(int i = 0; i < transactions.size(); i++) {
            totalAmount += transactions.get(i).getAmount();
        }
        return totalAmount;
    }

    public double updateTotalAfterDelete(Transaction transaction) {
        if(transaction.getAmount() > 0) {
            totalAmount -= transaction.getAmount();
        } else {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }

    private void showInitialActivity() {
        final Intent intent = new Intent(this, InitialActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        initialAmount = sharedPreferences.getFloat(INITIAL_AMOUNT, 0);
    }

    public void updateViews() {
        tvTotalAmount.setText(String.valueOf(initialAmount));
    }

    public class openActivityAsync extends AsyncTask<Void, Void, Void> {
        private Context context;

        public openActivityAsync(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showInitialActivity();
        }
    }
}
