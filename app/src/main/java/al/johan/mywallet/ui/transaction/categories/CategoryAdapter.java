package al.johan.mywallet.ui.transaction.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import al.johan.mywallet.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner_row, parent, false
            );
        }

        ImageView iVCategory = convertView.findViewById(R.id.iV_category_image);
        TextView tVCategoryName = convertView.findViewById(R.id.tVCategoryName);

        CategoryItem currentItem = getItem(position);

        if (currentItem != null) {
            iVCategory.setImageResource(currentItem.getCategoryImage());
            tVCategoryName.setText(currentItem.getCategoryName());
        }

        return convertView;
    }
}
