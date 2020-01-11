package al.johan.mywallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter {
    private List<Transaction> transactions = new ArrayList<>();

    class PositiveTransactionHolder extends RecyclerView.ViewHolder {
        private ImageView iVTransactionIcon;
        private TextView tVTransactionAmount;
        private TextView tVTransactionDescription;

        public PositiveTransactionHolder(@NonNull View itemView) {
            super(itemView);
            iVTransactionIcon = itemView.findViewById(R.id.iVPositiveTransactionIcon);
            tVTransactionAmount = itemView.findViewById(R.id.tvPositiveTransactionAmount);
            tVTransactionDescription = itemView.findViewById(R.id.tvPositiveTransactionDescription);
        }
    }

    class NegativeTransactionHolder extends RecyclerView.ViewHolder {
        private ImageView iVTransactionIcon;
        private TextView tVTransactionAmount;
        private TextView tVTransactionDescription;

        public NegativeTransactionHolder(@NonNull View itemView) {
            super(itemView);
            iVTransactionIcon = itemView.findViewById(R.id.iVNegativeTransactionIcon);
            tVTransactionAmount = itemView.findViewById(R.id.tvNegativeTransactionAmount);
            tVTransactionDescription = itemView.findViewById(R.id.tvNegativeTransactionDescription);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Transaction currentTransaction = transactions.get(position);
        if (currentTransaction.getAmount() > 0) {
            return 0;
        } else {
            return 1;
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
        switch (holder.getItemViewType()) {
            case 0:
                ((PositiveTransactionHolder) holder).iVTransactionIcon.setImageResource(R.drawable.dollar_icon);
                ((PositiveTransactionHolder) holder).tVTransactionAmount.setText(String.valueOf(currentTransaction.getAmount()));
                ((PositiveTransactionHolder) holder).tVTransactionDescription.setText(currentTransaction.getDescription());
                break;
            case 1:
                ((NegativeTransactionHolder) holder).iVTransactionIcon.setImageResource(R.drawable.dollar_icon);
                ((NegativeTransactionHolder) holder).tVTransactionAmount.setText(String.valueOf(currentTransaction.getAmount()));
                ((NegativeTransactionHolder) holder).tVTransactionDescription.setText(currentTransaction.getDescription());
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

}
