package al.johan.mywallet.ui.home;

import al.johan.mywallet.ui.chart.ChartActivity;
import al.johan.mywallet.ui.intro.IntroActivity;
import al.johan.mywallet.ui.transaction.CreateTransactionDialog;
import al.johan.mywallet.R;
import al.johan.mywallet.data.db.entities.Transaction;
import al.johan.mywallet.ui.transaction.TransactionAdapter;
import al.johan.mywallet.ui.transaction.TransactionViewModel;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CreateTransactionDialog.CreateTransactionDialogListener {
    private TransactionViewModel transactionViewModel;
    private TextView tvTotalAmount, tvEmpty, tvEmptyDesc;
    private FloatingActionButton btnChart;
    double totalAmount;
    public static String SELECTED_MONTH_NUMBER;
    Spinner monthsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvEmpty = findViewById(R.id.tvEmptyText);
        tvEmptyDesc = findViewById(R.id.tVEmptyTransactionDesc);
        tvEmpty.setVisibility(View.GONE);
        tvEmptyDesc.setVisibility(View.GONE);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnChart = findViewById(R.id.btnChart);
        monthsSpinner = findViewById(R.id.monthSpinner);

        btnChart.setVisibility(View.GONE);

        //Get current month
        DateFormat sortable = new SimpleDateFormat("MM");
        Date now = Calendar.getInstance().getTime();
        final String currentMonth = sortable.format(now);

        FloatingActionButton btnAddTransaction = findViewById(R.id.btnAddTransaction);
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.transaction_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TransactionAdapter adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        final Map<String, String> m = new LinkedHashMap<>();
        m.put("Jan", "01");
        m.put("Feb", "02");
        m.put("Mar", "03");
        m.put("Apr", "04");
        m.put("May", "05");
        m.put("June", "06");
        m.put("July", "07");
        m.put("Aug", "08");
        m.put("Sept", "09");
        m.put("Oct", "10");
        m.put("Nov", "11");
        m.put("Dec", "12");

        ArrayList<String> months = new ArrayList<>();
        for (Map.Entry<String, String> entry : m.entrySet()) {
            months.add(entry.getKey());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, months);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_custom);
        monthsSpinner.setAdapter(arrayAdapter);
        String currentMonthLabel = "";
        switch (currentMonth) {
            case "01":
                currentMonthLabel = "Jan";
                break;
            case "02":
                currentMonthLabel = "Feb";
                break;
            case "03":
                currentMonthLabel = "Mar";
                break;
            case "04":
                currentMonthLabel = "Apr";
                break;
            case "05":
                currentMonthLabel = "May";
                break;
            case "06":
                currentMonthLabel = "June";
                break;
            case "07":
                currentMonthLabel = "July";
                break;
            case "08":
                currentMonthLabel = "Aug";
                break;
            case "09":
                currentMonthLabel = "Sept";
                break;
            case "10":
                currentMonthLabel = "Oct";
                break;
            case "11":
                currentMonthLabel = "Nov";
                break;
            case "12":
                currentMonthLabel = "Dec";
                break;
        }
        int Position = arrayAdapter.getPosition(currentMonthLabel);
        monthsSpinner.setSelection(Position);

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        final LiveData<List<Transaction>> viewModelData = transactionViewModel.getTransactionsByMonth(currentMonth);
        viewModelData.observe(MainActivity.this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
                tvTotalAmount.setText(String.valueOf(calculateTotal(transactions)));
                if (transactions.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmptyDesc.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    tvEmptyDesc.setVisibility(View.GONE);
                }
            }
        });

        monthsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getId()) {
                    case R.id.monthSpinner:
                        String monthName = parent.getItemAtPosition(position).toString();
                        String monthNumber = m.get(monthName);
                        SELECTED_MONTH_NUMBER = monthNumber;

                        //too many observers, memory issue?
                        transactionViewModel.getAllNegativeTransactionsByMonth(monthNumber).observe(MainActivity.this, new Observer<List<Transaction>>() {
                            @Override
                            public void onChanged(List<Transaction> transactions) {
                                if(!transactions.isEmpty()) {
                                    btnChart.setVisibility(View.VISIBLE);
                                } else {
                                    btnChart.setVisibility(View.GONE);
                                }
                            }
                        });

                        final LiveData<List<Transaction>> viewModelData = transactionViewModel.getTransactionsByMonth(monthNumber);
                        viewModelData.observe(MainActivity.this, new Observer<List<Transaction>>() {
                            @Override
                            public void onChanged(List<Transaction> transactions) {
                                adapter.setTransactions(transactions);
                                tvTotalAmount.setText(String.valueOf(calculateTotal(transactions)));
                                if (transactions.isEmpty()) {
                                    recyclerView.setVisibility(View.GONE);
                                    tvEmpty.setVisibility(View.VISIBLE);
                                    tvEmptyDesc.setVisibility(View.VISIBLE);
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    tvEmpty.setVisibility(View.GONE);
                                    tvEmptyDesc.setVisibility(View.GONE);
                                }
                            }
                        });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                Toast.makeText(MainActivity.this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    intent.putExtra("SELECTED_MONTH_NUMBER", SELECTED_MONTH_NUMBER);
                    startActivity(intent);
            }
        });
    }

    private void openDialog() {
        CreateTransactionDialog createTransactionDialog = new CreateTransactionDialog();
        createTransactionDialog.show(getSupportFragmentManager(), "create fragment dialog");
    }

    @Override
    public void applyCreation(String description, double amount, String category) {
        DateFormat sortable = new SimpleDateFormat("yyyy-MM-dd");
        Date now = Calendar.getInstance().getTime();
        String timestampish = sortable.format(now);

        Transaction transaction = new Transaction(description, amount, timestampish, category);
        transactionViewModel.insert(transaction);

        Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show();
    }

    public double calculateTotal(List<Transaction> transactions) {
        totalAmount = 0;
        for(int i = 0; i < transactions.size(); i++) {
            totalAmount += transactions.get(i).getAmount();
        }
        return roundToTwoDigits(totalAmount);
    }

    public double roundToTwoDigits(double number) {
        number = Math.round(number * 100.0) / 100.0;
        return number;
    }
}