package com.appdeveloper.allenwang.mvp.ui.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdeveloper.allenwang.mvp.R;
import com.appdeveloper.allenwang.mvp.model.MovieBrief;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by allenwang on 2018/8/27.
 */

public class MainRecyclerviewAdapter extends RecyclerView.Adapter<MainRecyclerviewAdapter.MainViewHolder> {
    private Context context;
    private List<MovieBrief> data = new ArrayList<>();
    private ItemListener itemListener;

    public void updateGameDatas(List<MovieBrief> items) {
        data.clear();
        if (items != null) {
            data.addAll(items);
        }
        this.notifyDataSetChanged();
    }

    public MainRecyclerviewAdapter(Context context, ItemListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_view_item, parent, false);
        return new MainViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        if(holder != null) {
            MovieBrief data = this.data.get(position);
            MainViewHolder viewHolder = holder;
            viewHolder.gameName.setText(data.getTitle());
        }


    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) != null ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MovieBrief getItem(int position) {
        return data.get(position);
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textView_title)
        TextView gameName;

        @BindView(R.id.card_view)
        CardView cardView;

        private ItemListener mItemListener;

        public MainViewHolder(View itemView, ItemListener itemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mItemListener = itemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieBrief item = getItem(position);
            mItemListener.onClick(item);
        }
    }

    public interface ItemListener {
        void onClick(MovieBrief item);
    }
}
