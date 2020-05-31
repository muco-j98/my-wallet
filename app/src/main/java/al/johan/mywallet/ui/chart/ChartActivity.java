package al.johan.mywallet.ui.chart;

import al.johan.mywallet.R;
import al.johan.mywallet.data.db.entities.Transaction;
import al.johan.mywallet.ui.transaction.TransactionViewModel;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    PieChart mPieChart;
    float total_amount = 0, total_food = 0, total_transportation = 0, total_clothing = 0, total_housing = 0;
    final List<PieEntry> value = new ArrayList<>();
    TextView tvCategoryNameVariable, tvAmountPerCategory, tVNoCategorySelected, tvNoExpenses, tvMonthLabel, tvMonthTotal;
    RelativeLayout rLCategoryAmount;
    LinearLayout lLChartLayout;
    private static final DecimalFormat FORMAT = new DecimalFormat("$#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mPieChart = findViewById(R.id.piechart);
        tvAmountPerCategory = findViewById(R.id.tvAmountPerCategory);
        tvCategoryNameVariable = findViewById(R.id.tVCategoryNameVariable);
        tVNoCategorySelected = findViewById(R.id.tVNoCategorySelected);
        tvNoExpenses = findViewById(R.id.tVNoTransactions);
        rLCategoryAmount = findViewById(R.id.rLCategoryAmount);
        tvMonthLabel = findViewById(R.id.chart_month_label);
        tvMonthTotal = findViewById(R.id.chart_month_total);
        lLChartLayout = findViewById(R.id.chart_layout);


        String monthNumber = getIntent().getStringExtra("SELECTED_MONTH_NUMBER");
        String monthLabel = getIntent().getStringExtra("SELECTED_MONTH_LABEL");

        tvMonthLabel.setText(monthLabel);

        TransactionViewModel transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.getAllNegativeTransactionsByMonth(monthNumber).observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                categoryTotalCalculation(transactions);

                if (total_food != 0) {
                    value.add(new PieEntry(total_food/total_amount, "Food"));
                }
                if (total_transportation != 0) {
                    value.add(new PieEntry(total_transportation / total_amount, "Transportation"));
                }
                if (total_clothing != 0) {
                    value.add(new PieEntry(total_clothing / total_amount, "Clothing"));
                }
                if (total_housing != 0) {
                    value.add(new PieEntry(total_housing/total_amount, "Housing"));
                }

                if (transactions.size() != 0) {
                    chartStyling();
                    total_amount = (float) (Math.round(total_amount * 100.0) / 100.0);
                    tvMonthTotal.setText(NumberFormat.getCurrencyInstance(Locale.US).format(-1 * total_amount));
                } else {
                    lLChartLayout.setVisibility(View.GONE);
                    tvNoExpenses.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void chartStyling() {
        PieDataSet pieDataSet = new PieDataSet(value, "");
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15);
        mPieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        mPieChart.spin( 500,0,-360f, Easing.EasingOption.EaseInOutQuad);
        mPieChart.setCenterText("Expenses \nDistribution");
        mPieChart.setCenterTextSize(20);
        pieData.setValueFormatter(new PercentFormatter(new DecimalFormat("###,###,##0.0")));
        mPieChart.setUsePercentValues(true);
        mPieChart.setNoDataText("There are no expenses currently");
        mPieChart.getDescription().setText("");
        mPieChart.setEntryLabelTextSize(17);
        mPieChart.setHoleColor(25227146);
        mPieChart.setCenterTextColor(Color.rgb(237,185,192));
        mPieChart.setRotationEnabled(false);
        mPieChart.setTouchEnabled(true);
        mPieChart.setOnChartValueSelectedListener(this);
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
    }

    public void categoryTotalCalculation(List<Transaction> transactions) {
        for (int i = 0; i < transactions.size(); i++) {
            total_amount += transactions.get(i).getAmount();
            switch (transactions.get(i).getCategory()) {
                case "Food":
                    total_food += transactions.get(i).getAmount();
                    break;
                case "Transportation":
                    total_transportation += transactions.get(i).getAmount();
                    break;
                case "Clothing":
                    total_clothing += transactions.get(i).getAmount();
                    break;
                case "Housing":
                    total_housing += transactions.get(i).getAmount();
                    break;
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        float y = e.getY();
        PieEntry pe = (PieEntry) e;
        double categoryAmount = -1 * (y * total_amount);
        categoryAmount = Math.round(categoryAmount * 100.0) / 100.0;

        tvCategoryNameVariable.setText(pe.getLabel());
        tvAmountPerCategory.setText(String.valueOf(categoryAmount));
    }

    @Override
    public void onNothingSelected() {
        Toast.makeText(this, "Select a category.", Toast.LENGTH_SHORT).show();
    }
}
