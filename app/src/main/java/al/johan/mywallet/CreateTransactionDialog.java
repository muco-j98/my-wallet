package al.johan.mywallet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateTransactionDialog extends AppCompatDialogFragment {
    private TextInputEditText etDescription;
    private TextInputEditText etAmount;
    private CreateTransactionDialogListener listener;
    private ArrayList<CategoryItem> categoryList;
    private CategoryAdapter categoryAdapter;
    private String mCategory;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.create_transaction, null);
        initList();

        final Spinner spinnerCategories = view.findViewById(R.id.spinner_categories);
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        spinnerCategories.setAdapter(categoryAdapter);
        spinnerCategories.setVisibility(View.GONE);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem clickedItem = (CategoryItem) parent.getItemAtPosition(position);
                String category = clickedItem.getCategoryName();
                mCategory = category;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view)
                .setTitle("Add Transaction")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveTransaction();
                    }
                });

        etDescription = view.findViewById(R.id.etCreateTransactionDescription);
        etAmount = view.findViewById(R.id.etCreateTransactionAmount);

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double amount = Double.parseDouble(s.toString());
                    if (amount < 0)
                        spinnerCategories.setVisibility(View.VISIBLE);
                }
                catch (NumberFormatException e) {
                    spinnerCategories.setVisibility(View.GONE);
                }
            }
        });

        return builder.create();
    }

    private void saveTransaction() {
        String description = etDescription.getText().toString().trim();

        if (description.isEmpty() || etAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Please insert a title and a value", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(etAmount.getText().toString()) == 0 ) {
            Toast.makeText(getContext(), "The amount cannot be 0", Toast.LENGTH_SHORT).show();
        } else {
            double amount = Double.parseDouble(etAmount.getText().toString());
            listener.applyCreation(description, amount, mCategory);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (CreateTransactionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CreateTransactionDialogListener");
        }
    }

    public interface CreateTransactionDialogListener{
        void applyCreation(String description, double amount, String category);
    }

    private void initList() {
        categoryList = new ArrayList<>();
        categoryList.add(new CategoryItem("Food", R.drawable.food));
        categoryList.add(new CategoryItem("Transportation", R.drawable.bus));
        categoryList.add(new CategoryItem("Clothing", R.drawable.tshirt));
        categoryList.add(new CategoryItem("Housing", R.drawable.home));
    }
}


