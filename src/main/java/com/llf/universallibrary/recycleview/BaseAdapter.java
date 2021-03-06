package com.llf.universallibrary.recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2016/7/21.
 * 封装的Adapter
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private int itemId;
    private Context mContext;
    private List<T> datas;
    private OnItemClickListener mOnItemClickListener;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_Normal = 1;
    private int headViewRes;

    public BaseAdapter(Context context, int itemId) {
        this.itemId = itemId;
        this.mContext = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (headViewRes == 0) return TYPE_Normal;
        if (position == 0) return TYPE_HEAD;
        return TYPE_Normal;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder;
        if (headViewRes != 0 && viewType == TYPE_HEAD) {
            viewHolder = new BaseViewHolder(LayoutInflater.from(mContext).inflate(headViewRes, parent, false));
        } else {
            viewHolder = new BaseViewHolder(LayoutInflater.from(mContext).inflate(itemId, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (getItemViewType(position) == TYPE_HEAD) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(headViewRes == 0 ? position : position - 1);
                }
            }
        });
        convert(holder, datas.get(headViewRes == 0 ? position : position - 1));
    }

    public abstract void convert(BaseViewHolder viewHolder, T item);

    public interface OnItemClickListener {
        void onItemClick(int positon);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setHeadView(int headViewRes) {
        this.headViewRes = headViewRes;
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return headViewRes == 0 ? datas.size() : datas.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEAD
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    public void Clear(){
        this.datas.clear();
        notifyDataSetChanged();
    }

    public void removeHeadView(){
        if(headViewRes != 0)
        notifyItemRemoved(0);
    }
}
