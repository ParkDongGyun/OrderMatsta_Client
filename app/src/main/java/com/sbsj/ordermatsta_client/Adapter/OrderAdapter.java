package com.sbsj.ordermatsta_client.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderInfoColumn;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    ArrayList<GoodsInfoColumn> goodslist = new ArrayList<>();
    ArrayList<OrderInfoColumn> orderlist = new ArrayList<>();

    public OrderAdapter(Context context, ArrayList<OrderInfoColumn> orderlist, ArrayList<GoodsInfoColumn> goodslist, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.goodslist = goodslist;
        this.orderlist = orderlist;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
//        holder.tv_goodsname.setText(BaseActivity.orderlist.get(position).getGoodsname());
//        holder.tv_goodsprice.setText(((BaseActivity)context).printPrice(context.getResources().getString(R.string.oneprice_msg), orderlist.get(position).getGoodsprice()));
//        holder.tv_amount.setText(Integer.toString(BaseActivity.orderlist.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public OrderInfoColumn getItem(int position) {
        return orderlist.get(position);
    }

    public void deleteitem(int position) {
        BaseActivity.orderlist.remove(position);
        notifyDataSetChanged();
    }

    public int setAmount(int position, boolean isplus) {
//        int amount = orderlist.get(position).getAmount();
//
//        if (isplus)
//            amount++;
//        else
//            amount--;
//
//        if (amount < 0 || amount > 10)
//            return 0;
//
//        BaseActivity.orderlist.get(position).setAmount(amount);
//        notifyDataSetChanged();
//
//        return amount;
        return 0;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_goodsname;
        TextView tv_goodsprice;
        Button btn_up;
        TextView tv_amount;
        Button btn_down;
        Button btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_goodsname = itemView.findViewById(R.id.tv_ol_goodname);
            tv_goodsprice = itemView.findViewById(R.id.tv_ol_goodprice);
            btn_up = itemView.findViewById(R.id.btn_ol_upAmount);

            btn_up = itemView.findViewById(R.id.btn_ol_upAmount);
            btn_up.setOnClickListener(this);
            tv_amount = itemView.findViewById(R.id.tv_ol_amount);
            btn_down = itemView.findViewById(R.id.btn_ol_downAmount);
            btn_down.setOnClickListener(this);
            btn_delete = itemView.findViewById(R.id.btn_ol_delete);
            btn_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}