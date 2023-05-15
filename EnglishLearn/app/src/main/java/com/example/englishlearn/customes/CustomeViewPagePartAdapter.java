package com.example.englishlearn.customes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.englishlearn.R;
import com.example.englishlearn.api.APIConst;
import com.example.englishlearn.models.Part;

import java.util.List;

public class CustomeViewPagePartAdapter extends RecyclerView.Adapter<CustomeViewPagePartAdapter.ItemPartHolder> {
    private List<Part> mList;
    private OnClickButtonLear mOnClickButtonLear;

    public interface OnClickButtonLear{
        void OnclickButtonLearn(Part part);
    }

    public CustomeViewPagePartAdapter(List<Part> mList, OnClickButtonLear mOnClickButtonLear) {
        this.mList = mList;
        this.mOnClickButtonLear = mOnClickButtonLear;
    }

    @NonNull
    @Override
    public ItemPartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_iemt_viewpager_parts, parent, false);
        return new ItemPartHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPartHolder holder, int position) {
        Part part = mList.get(position);
        if (part == null) {
            return;
        }
        holder.titlePart.setText(part.getName());
        Glide.with(holder.layout).load(APIConst.baseUrl + APIConst.storeImage + part.getAvatar()).into(holder.imagePart);

        holder.btnPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickButtonLear.OnclickButtonLearn(part);
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

    public class ItemPartHolder extends RecyclerView.ViewHolder {
        private ImageView imagePart;
        private TextView titlePart;
        private Button btnPart;
        private CardView layout;

        public ItemPartHolder(@NonNull View itemView) {
            super(itemView);
            imagePart = itemView.findViewById(R.id.livp_image);
            titlePart = itemView.findViewById(R.id.livp_title);
            btnPart = itemView.findViewById(R.id.livp_btn);
            layout = itemView.findViewById(R.id.livp_layout);
        }
    }
}
