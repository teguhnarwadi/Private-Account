package com.narwadi.saveyouraccount.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.narwadi.saveyouraccount.R;
import com.narwadi.saveyouraccount.model.AccountModel;

import java.util.List;

/**
 * Created by DEKSTOP on 03/12/2016.
 */
public class RecycleBaseAdapter extends RecyclerView.Adapter<RecycleBaseAdapter.CustomViewHolder> {

    private List<AccountModel> accountList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public RecycleBaseAdapter(Context context, List<AccountModel> accountList) {
        this.accountList = accountList;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_account, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final AccountModel account = accountList.get(i);

        //Setting text view title
        customViewHolder.tvName.setText(account.getName());
        customViewHolder.tvEmail.setText(account.getEmail());

        //Setting click listener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(account);
            }
        };

        //Setting long click listener
        View.OnLongClickListener listenerLong = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(context, "Long", Toast.LENGTH_LONG).show();
                onItemLongClickListener.onItemLongClick(account);
                return true;
            }
        };

        customViewHolder.tvName.setOnClickListener(listener);
        customViewHolder.tvEmail.setOnClickListener(listener);

        customViewHolder.tvName.setOnLongClickListener(listenerLong);
        customViewHolder.tvEmail.setOnLongClickListener(listenerLong);
    }

    @Override
    public int getItemCount() {
        return (null != accountList ? accountList.size() : 0);
    }

    public void removeItem(int position) {
        accountList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, accountList.size());
    }


    /**
     * class custom view holder
     */
    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvName;
        protected TextView tvEmail;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.textView_name);
            this.tvEmail = (TextView) itemView.findViewById(R.id.textView_email);
        }
    }

    /**
     * define interface on item click listener
     */
    public interface OnItemClickListener {
        void onItemClick(AccountModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * define interface on item long click listener
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(AccountModel item);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
