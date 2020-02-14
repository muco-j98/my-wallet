package al.johan.mywallet.ui.initial;

import al.johan.mywallet.R;
import al.johan.mywallet.ui.home.MainActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InitialActivity extends AppCompatActivity {
    EditText etInitialAmount;
    Button btnSubmit;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INITIAL_AMOUNT = "initialAmount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_activity);

        etInitialAmount = findViewById(R.id.etInitialAmount);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double initialAmount = Double.parseDouble(etInitialAmount.getText().toString().trim());
                if (etInitialAmount.getText().toString().isEmpty()) {
                    etInitialAmount.setError("Enter Amount");
                } else if (initialAmount == 0) {
                    etInitialAmount.setError("Amount cannot be 0");
                } else if (initialAmount < 0) {
                    etInitialAmount.setError("Amount cannot be negative");
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("initialAmount", initialAmount);
                    setResult(RESULT_OK, intent);
                    saveData();
                    finish();
                }
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(INITIAL_AMOUNT, Float.parseFloat(etInitialAmount.getText().toString()));
        editor.apply();
    }

    //Does not allow user to press the back button in the initial activity
    @Override
    public void onBackPressed() {

    }
}
