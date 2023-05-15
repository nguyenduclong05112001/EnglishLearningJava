package com.example.englishlearn.customes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.models.Level;

import java.util.List;

public class CustomeRecyclerStudyAdapter extends RecyclerView.Adapter<CustomeRecyclerStudyAdapter.ItemtViewHolder>{
    private List<Level> mList;
    private handlerOnclickItemLevel mhandlerOnclickItemLevel;

    public interface handlerOnclickItemLevel{
        void OnclickItemLevl(Level level);
    }

    public CustomeRecyclerStudyAdapter(List<Level> mList, handlerOnclickItemLevel mhandlerOnclickItemLevel) {
        this.mList = mList;
        this.mhandlerOnclickItemLevel = mhandlerOnclickItemLevel;
    }

    @NonNull
    @Override
    public ItemtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        return new ItemtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemtViewHolder holder, int position) {
        Level level = mList.get(position);
        if(level == null){
            return;
        }
        Glide.with(holder.layout).load(APIConst.baseUrl + APIConst.storeImage + level.getAvatar()).into(holder.image);
        holder.name.setText(level.getName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mhandlerOnclickItemLevel.OnclickItemLevl(level);
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
        private LinearLayout layout;
        public ItemtViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ir_image);
            name = itemView.findViewById(R.id.ir_name);
            layout = itemView.findViewById(R.id.ir_layout);
        }
    }
}
