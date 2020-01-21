package al.johan.mywallet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateTransactionDialog extends AppCompatDialogFragment {
    private EditText etDescription;
    private EditText etAmount;
    private CreateTransactionDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_transaction, null);

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
            listener.applyCreation(description, amount);
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
        void applyCreation(String description, double amount);
    }
}
