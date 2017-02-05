package com.example.issa.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Philippe on 04/02/2017.
 */

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }

        ProductViewHolder viewHolder = (ProductViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ProductViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
           // viewHolder.avatar = (TextView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Product> products
        Product product = getItem(position);
        viewHolder.name.setText(product.getname());
        viewHolder.text.setText(product.getText());
        viewHolder.avatar.setText(product.getname());

        return convertView;
    }

    private class ProductViewHolder{
        public TextView name;
        public TextView text;
        public TextView avatar;

    }
}
