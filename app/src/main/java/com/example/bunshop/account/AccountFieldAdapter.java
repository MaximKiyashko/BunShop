package com.example.bunshop.account;

import android.content.Context;
import android.icu.text.MessagePattern;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bunshop.R;

import java.util.ArrayList;

public class AccountFieldAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater layoutInflater;
    private ArrayList<AccountField> accountFields;

    public AccountFieldAdapter(Context context, ArrayList<AccountField> accountFields) {
        ctx = context;
        this.accountFields = accountFields;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return accountFields.size();
    }

    @Override
    public Object getItem(int i) {
        return accountFields.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.account_item, viewGroup, false);
        }

        AccountField accountField = getAccountField(i);
        ((TextView) view.findViewById(R.id.tvCaptionAccItem)).setText(accountField.getCaption());
        ((TextView) view.findViewById(R.id.tvFieldAccItem)).setText(accountField.getField());

        return view;
    }

    AccountField getAccountField(int position) {
        return ((AccountField) getItem(position));
    }
}
