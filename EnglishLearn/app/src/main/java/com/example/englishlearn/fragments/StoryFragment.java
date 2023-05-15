package com.example.englishlearn.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.ContentOfStoryActivity;
import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.customes.CustomeRecyclerStory;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Story;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryFragment extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private HomeActivity mHomeActivity;
    private CustomeRecyclerStory adapter;
    private ProgressDialog mDialog;
    private List<Story> mListStory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_story, container, false);

        setListLevel();

        return mView;
    }

    private void setListLevel() {
        mHomeActivity = (HomeActivity) getActivity();
        mDialog = new ProgressDialog(mHomeActivity);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));

        mListStory = new ArrayList<>();

        mDialog.show();
        APIServer.api.getStory().enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                for (Story item : response.body()) {
                    mListStory.add(item);
                }
                mDialog.dismiss();
                mappingElements();
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mappingElements() {
        mRecyclerView = mView.findViewById(R.id.fstory_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(mHomeActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new CustomeRecyclerStory(mListStory, new CustomeRecyclerStory.OnclickItemofStory() {
            @Override
            public void onClickItem(Story story) {
                toActivity(ContentOfStoryActivity.class,story);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void toActivity(Class<?> to, Story story) {
        Intent intent = new Intent(mHomeActivity,to);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstSendData.KEY_STORY_OBJECT,story);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}