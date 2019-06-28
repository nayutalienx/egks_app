package com.example.demochat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CardListAdapter extends ArrayAdapter<EgksCard> {

    private Context mContext;
    int mResourse;

    public CardListAdapter(Context context, int resource, ArrayList<EgksCard> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourse = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String number = getItem(position).getNumber();
        String balance = getItem(position).getBalance();  //"0\u20BD"
        Integer id = getItem(position).getId();



        if(balance.equals("0\u20BD")){

        } else {
            balance = balance + "\u20BD";
        }


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResourse,parent,false);

        TextView tvName = convertView.findViewById(R.id.cardNameHolder);
        TextView tvNumber = convertView.findViewById(R.id.cardNumberItem);
        TextView tvBalance = convertView.findViewById(R.id.cardBalanceItem);
        TextView tvId = convertView.findViewById(R.id.cardId);

        tvName.setText(name);
        tvNumber.setText(number);
        tvBalance.setText(balance);
        tvId.setText(id.toString());

        return convertView;

    }
}
