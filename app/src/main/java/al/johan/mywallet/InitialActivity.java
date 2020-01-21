package al.johan.mywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Result;

public class InitialActivity extends AppCompatActivity {
    EditText etInitialAmount;
    Button btnSubmit;

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
                    finish();
                }
            }
        });
    }
}
