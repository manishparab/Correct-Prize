package com.ksm.cp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksm.cp.Objects.ItemCategory;
import com.ksm.cp.R;

/**
 * Created by mparab on 2/7/2016.
 */
public class CustomAdapterItemCategory extends BaseAdapter {
    Context context;
    ItemCategory[] itemCategories;

    public CustomAdapterItemCategory(Context context, ItemCategory[] itemCategories)
    {
        this.context = context;
        this.itemCategories = itemCategories;
    }
    @Override
    public int getCount() {
        return itemCategories.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View workingview;
        if (convertView ==  null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            workingview = inflater.inflate(R.layout.item_category_list_item, parent,false);
        }
        else
        {
            workingview = convertView;
        }

        TextView textViewItemCategoryName = (TextView) workingview.findViewById(R.id.TextViewItemCategoryName);
        TextView textViewItemCategoryId = (TextView) workingview.findViewById(R.id.TextViewItemCategoryId);
        ItemCategory item =  itemCategories[position];
        textViewItemCategoryName.setText(item.Name);
        textViewItemCategoryId.setText(String.valueOf(item.Id));
        return workingview;

    }
}
