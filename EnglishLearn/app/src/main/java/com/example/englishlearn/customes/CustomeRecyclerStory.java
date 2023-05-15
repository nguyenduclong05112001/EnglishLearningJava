package com.example.englishlearn.customes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.models.Story;

import java.util.List;

public class CustomeRecyclerStory extends RecyclerView.Adapter<CustomeRecyclerStory.ItemtViewHolder>{

    public interface OnclickItemofStory{
        void onClickItem(Story story);
    }

    private List<Story> mList;
    private OnclickItemofStory mOnclickItemofStory;

    public CustomeRecyclerStory(List<Story> mList, OnclickItemofStory mOnclickItemofStory) {
        this.mList = mList;
        this.mOnclickItemofStory = mOnclickItemofStory;
    }

    @NonNull
    @Override
    public ItemtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_story, parent, false);
        return new ItemtViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemtViewHolder holder, int position) {
        Story story = mList.get(position);
        if(story == null){
            return;
        }
        Glide.with(holder.layout).load(APIConst.baseUrl + APIConst.storeImage + story.getAvatar()).into(holder.image);
        holder.name.setText(story.getName());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnclickItemofStory.onClickItem(story);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList.size() == 0){
            return 0;
        }
        return mList.size();
    }

    protected class ItemtViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView name;
        private CardView layout;

        public ItemtViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.lis_imgstory);
            name = itemView.findViewById(R.id.lis_name);
            layout = itemView.findViewById(R.id.lis_layout);
        }
    }
}
