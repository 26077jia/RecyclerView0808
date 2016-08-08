package com.example.jiajia.recyclerview0808;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jiajia on 2016/8/8.
 */
public class MyLinearAdapter extends RecyclerView.Adapter<MyLinearAdapter.MyViewHolder>{
    private List<Beauty> beauties;
    private LayoutInflater inflater;

    public MyLinearAdapter(List<Beauty> datas) {
        this.beauties = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_linear,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //set value
//        holder.tv_item.setText();
        holder.tv_item.setText("图"+position);
        Picasso.with().load(beauties.get(position).getUrl()).into(holder.iv_item);
    }

    @Override
    public int getItemCount() {
        return beauties.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item;
        private TextView tv_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_linear_item);
            iv_item = (ImageView) itemView.findViewById(R.id.iv_linear_item);
        }
    }
}
