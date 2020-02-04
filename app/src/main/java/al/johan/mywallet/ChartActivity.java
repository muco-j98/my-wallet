package al.johan.mywallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {
    PieChart mPieChart;
    float total_amount = 0, total_food = 0, total_transportation = 0, total_clothing = 0, total_housing = 0;
    final List<PieEntry> value = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        mPieChart = findViewById(R.id.piechart);

        TransactionViewModel transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.getAllNegativeTransactionsBetweenDate().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {

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
                chartStyling();
            }
        });

        //Add recyclerview for each category on the pie chart.
        //When clicked it shows the spendings for that category.
    }

    public void chartStyling() {
        PieDataSet pieDataSet = new PieDataSet(value, "");
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(15);
        mPieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        mPieChart.spin( 500,0,-360f, Easing.EasingOption.EaseInOutQuad);
        mPieChart.setTouchEnabled(false);
        mPieChart.setCenterText("Expenses \nDistribution");
        mPieChart.setCenterTextSize(20);
        pieData.setValueFormatter(new PercentFormatter(new DecimalFormat("###,###,##0.0")));
        mPieChart.setUsePercentValues(true);
        mPieChart.setNoDataText("There are no expenses currently");
        mPieChart.getDescription().setText("");
        mPieChart.setEntryLabelTextSize(17);
        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
    }
}
