package ru.andreya108.dumcharmarkupinfo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PartitionSizesAdapter extends BaseAdapter {
    List<PartitionSizeInfo> items;
    LayoutInflater inflater;

    public PartitionSizesAdapter(LayoutInflater inflater, List<PartitionSizeInfo> items)
    {
        super();
        this.inflater = inflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PartitionSizeInfo item = (PartitionSizeInfo) getItem(position);

        View rowView=inflater.inflate(R.layout.part_size_list_item, null, true);

        TextView left = (TextView) rowView.findViewById(R.id.left);
        left.setText(item.name);

        TextView right = (TextView) rowView.findViewById(R.id.right);
        right.setText( String.format(inflater.getContext().getString(R.string.size_mb), item.size / DumcharUtil.MB) );

        return rowView;
    }
}
