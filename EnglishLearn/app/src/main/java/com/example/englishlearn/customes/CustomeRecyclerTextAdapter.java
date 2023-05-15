package com.example.englishlearn.customes;

import android.content.Context;
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
import com.example.englishlearn.models.TheTestOfUsers;

import java.util.List;

public class CustomeRecyclerTextAdapter extends RecyclerView.Adapter<CustomeRecyclerTextAdapter.ItemViewHolderText> {
    private List<TheTestOfUsers> mList;
    private HandlerOpenTheTest mHandlerOpenTheTest;
    private Context mContext;

    public CustomeRecyclerTextAdapter(List<TheTestOfUsers> mList, HandlerOpenTheTest mHandlerOpenTheTest) {
        this.mList = mList;
        this.mHandlerOpenTheTest = mHandlerOpenTheTest;
    }

    public interface HandlerOpenTheTest {
        void handlerOnclickItem(TheTestOfUsers item);
    }

    public void setDataList(List<TheTestOfUsers> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolderText onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_test_of_user, parent, false);
        mContext = parent.getContext();
        return new ItemViewHolderText(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolderText holder, int position) {
        TheTestOfUsers test = mList.get(position);

        if (test == null) {
            return;
        }

        String contentID = mContext.getResources().getString(R.string.id_of_the_test) + test.getId();
        String contentName = mContext.getResources().getString(R.string.name_of_the_test) + test.getName();
        String contentUser = mContext.getResources().getString(R.string.id_of_maker) + test.getIduser();
        String contentDate = mContext.getResources().getString(R.string.date_created) + test.getDatecreate();

        holder.id.setText(contentID);
        holder.name.setText(contentName);
        holder.user.setText(contentUser);
        holder.date.setText(contentDate);
        Glide.with(holder.layout).load(APIConst.baseUrl + APIConst.storeImage + test.getAvatar()).error(R.drawable.avatardefault).into(holder.image);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandlerOpenTheTest.handlerOnclickItem(test);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList.size() == 0) {
            return 0;
        }
        return mList.size();
    }

    public class ItemViewHolderText extends RecyclerView.ViewHolder {
        private CardView layout;
        private ImageView image;
        private TextView id, name, user, date;

        public ItemViewHolderText(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.itou_layout);
            image = itemView.findViewById(R.id.itou_image);
            id = itemView.findViewById(R.id.itou_lbid);
            name = itemView.findViewById(R.id.itou_lbname);
            user = itemView.findViewById(R.id.itou_lbuser);
            date = itemView.findViewById(R.id.itou_lbdate);
        }
    }
}
