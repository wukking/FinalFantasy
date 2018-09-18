package com.wyson.finalfantasy.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyson.finalfantasy.R;


import java.util.List;

/**
 * Fragment主页的Adapter
 *
 * @author : Wuyson
 * @date : 2018/9/18-16:03
 */
public class AppFragmentMainAdapter extends RecyclerView.Adapter<AppFragmentMainAdapter.FragmentMainViewHolder> {

    private Context mContext;
    private List<String> mList;

    public AppFragmentMainAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public FragmentMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_item_fragment_main, parent, false);
        return new FragmentMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentMainViewHolder holder, int position) {
        holder.tvText.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class FragmentMainViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;

        private FragmentMainViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }
}
