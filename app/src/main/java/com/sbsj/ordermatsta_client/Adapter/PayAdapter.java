package com.sbsj.ordermatsta_client.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderDetailInfoColumn> datalist;
    private ArrayList<GoodsInfoColumn> datalist_goods;

    public PayAdapter(Context context, ArrayList<OrderDetailInfoColumn> datalist, ArrayList<GoodsInfoColumn> datalist_goods) {
        this.context = context;
        this.datalist = datalist;
        this.datalist_goods = datalist_goods;
    }

    public void setDatalist(ArrayList<OrderDetailInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<OrderDetailInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public PayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paylist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayAdapter.ViewHolder holder, int position) {

        if(BaseActivity.goodslist.get(position).getImage() != null && !BaseActivity.goodslist.get(position).getImage().isEmpty())
            holder.iv_goodsimage.setImageBitmap(((BaseActivity) context).StringtoBitmap(BaseActivity.goodslist.get(position).getImage(), "StoreDetail_Activity"));
        else
            holder.iv_goodsimage.setImageDrawable(context.getDrawable(R.drawable.pictureimg));
        holder.tv_goodsname.setText(datalist_goods.get(position).getName());
        holder.tv_goodsprice.setText(((BaseActivity)context).printPrice(context.getResources().getString(R.string.oneprice_msg), datalist_goods.get(position).getPrice()));
        holder.tv_goodsamount.setText(String.format(context.getString(R.string.amountform), datalist.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_goodsimage;
        TextView tv_goodsname;
        TextView tv_goodsprice;
        TextView tv_goodsamount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_goodsimage = itemView.findViewById(R.id.iv_pay_goodsimage);
            tv_goodsname = itemView.findViewById(R.id.tv_pay_goodsname);
            tv_goodsprice = itemView.findViewById(R.id.tv_pay_goodsprice);
            tv_goodsamount = itemView.findViewById(R.id.tv_pay_goodsamount);
        }
    }
}