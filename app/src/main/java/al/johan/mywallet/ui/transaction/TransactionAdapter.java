package al.johan.mywallet.ui.transaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import al.johan.mywallet.R;
import al.johan.mywallet.data.db.entities.Transaction;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter {
    private List<Transaction> transactions = new ArrayList<>();

    class PositiveTransactionHolder extends RecyclerView.ViewHolder {
        private ImageView iVTransactionIcon;
        private TextView tVTransactionAmount;
        private TextView tVTransactionDescription;
        private TextView tvTransactionDate;

        public PositiveTransactionHolder(@NonNull View itemView) {
            super(itemView);
            iVTransactionIcon = itemView.findViewById(R.id.iVPositiveTransactionIcon);
            tVTransactionAmount = itemView.findViewById(R.id.tvPositiveTransactionAmount);
            tVTransactionDescription = itemView.findViewById(R.id.tvPositiveTransactionDescription);
            tvTransactionDate = itemView.findViewById(R.id.tvPositiveTransactionDate);
        }
    }

    class NegativeTransactionHolder extends RecyclerView.ViewHolder {
        private ImageView iVTransactionIcon;
        private TextView tVTransactionAmount;
        private TextView tVTransactionDescription;
        private TextView tvTransactionDate;
        private ImageView ivTransactionCategory;

        public NegativeTransactionHolder(@NonNull View itemView) {
            super(itemView);
            iVTransactionIcon = itemView.findViewById(R.id.iVNegativeTransactionIcon);
            tVTransactionAmount = itemView.findViewById(R.id.tvNegativeTransactionAmount);
            tVTransactionDescription = itemView.findViewById(R.id.tvNegativeTransactionDescription);
            tvTransactionDate = itemView.findViewById(R.id.tvNegativeTransactionDate);
            ivTransactionCategory = itemView.findViewById(R.id.iVTransactionCategory);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Transaction currentTransaction = transactions.get(position);
        if (transactions.isEmpty()) {
            return -1;
        } else {
            if (currentTransaction.getAmount() > 0) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.positive_transcation_item, parent, false);
                return new PositiveTransactionHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.negative_transaction_fragment, parent, false);
                return new NegativeTransactionHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Transaction currentTransaction = transactions.get(position);
        double transactionAmount = Math.round(currentTransaction.getAmount() * 100.0) / 100.0;
        switch (holder.getItemViewType()) {
            case 0:
                ((PositiveTransactionHolder) holder).iVTransactionIcon.setImageResource(R.drawable.dollar_icon);
                ((PositiveTransactionHolder) holder).tVTransactionAmount.setText(String.valueOf(transactionAmount));
                ((PositiveTransactionHolder) holder).tVTransactionDescription.setText(currentTransaction.getDescription());
                ((PositiveTransactionHolder) holder).tvTransactionDate.setText(currentTransaction.getCreationDate());
                break;
            case 1:
                ((NegativeTransactionHolder) holder).iVTransactionIcon.setImageResource(R.drawable.dollar_icon);
                ((NegativeTransactionHolder) holder).tVTransactionAmount.setText(String.valueOf(transactionAmount));
                ((NegativeTransactionHolder) holder).tVTransactionDescription.setText(currentTransaction.getDescription());
                ((NegativeTransactionHolder) holder).tvTransactionDate.setText(currentTransaction.getCreationDate());
                switch (currentTransaction.getCategory()) {
                    case "Food":
                        ((NegativeTransactionHolder) holder).ivTransactionCategory.setImageResource(R.drawable.food);
                        break;
                    case "Transportation":
                        ((NegativeTransactionHolder) holder).ivTransactionCategory.setImageResource(R.drawable.bus);
                        break;
                    case "Housing":
                        ((NegativeTransactionHolder) holder).ivTransactionCategory.setImageResource(R.drawable.home);
                        break;
                    case "Clothing":
                        ((NegativeTransactionHolder) holder).ivTransactionCategory.setImageResource(R.drawable.tshirt);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public Transaction getTransactionAt(int position) {
        return transactions.get(position);
    }
}
