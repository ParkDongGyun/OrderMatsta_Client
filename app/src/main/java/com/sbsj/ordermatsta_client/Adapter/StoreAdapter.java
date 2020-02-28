package com.sbsj.ordermatsta_client.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<OrderDetailInfoColumn> datalist;

    public StoreAdapter(Context context, ArrayList<OrderDetailInfoColumn> datalist, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.datalist = datalist;
        this.listener = onItemClickListener;
    }

    public void setDatalist(ArrayList<OrderDetailInfoColumn> datalist) {
        this.datalist = datalist;
    }

    public ArrayList<OrderDetailInfoColumn> getDatalist() {
        return datalist;
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StoreAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goodslist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        holder.tv_goodsname.setText(BaseActivity.goodslist.get(position).getName());

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String formattedStringPrice = myFormatter.format(BaseActivity.goodslist.get(position).getPrice());
        String text = String.format(context.getResources().getString(R.string.oneprice_msg), formattedStringPrice);
        holder.tv_goodsprice.setText(text);

        if(BaseActivity.goodslist.get(position).getImage() != null && !BaseActivity.goodslist.get(position).getImage().isEmpty())
            holder.iv_goodsimage.setImageBitmap(((BaseActivity) context).StringtoBitmap(BaseActivity.goodslist.get(position).getImage(), "StoreDetail_Activity"));
        else
            holder.iv_goodsimage.setImageDrawable(context.getDrawable(R.drawable.pictureimg));
        holder.tv_goodsamount.setText(Integer.toString(datalist.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public OrderDetailInfoColumn getItem(int position) {
        return datalist.get(position);
//		return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rl_item;
        ImageView iv_goodsimage;
        TextView tv_goodsname;
        TextView tv_goodsprice;
        TextView tv_goodsexplain;

        Button btn_minus;
        TextView tv_goodsamount;
        Button btn_plus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_item = itemView.findViewById(R.id.rl_goodsitem);
            iv_goodsimage = itemView.findViewById(R.id.iv_goodsimage);
            tv_goodsname = itemView.findViewById(R.id.tv_goodsname);
            tv_goodsprice = itemView.findViewById(R.id.tv_goodsprice);
            tv_goodsexplain = itemView.findViewById(R.id.tv_goodsexplain);

            btn_minus = itemView.findViewById(R.id.btn_goodsminus);
            tv_goodsamount = itemView.findViewById(R.id.tv_goodsamount);
            btn_plus = itemView.findViewById(R.id.btn_goodsplus);

            rl_item.setOnClickListener(this);
            btn_minus.setOnClickListener(this);
            btn_plus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}