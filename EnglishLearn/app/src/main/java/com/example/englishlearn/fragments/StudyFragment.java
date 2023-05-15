package com.example.englishlearn.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.activities.PartStudyActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.customes.CustomeRecyclerStudyAdapter;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.Level;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyFragment extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private HomeActivity mHomeActivity;
    private CustomeRecyclerStudyAdapter adapter;
    private ProgressDialog mDialog;
    private List<Level> mListLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_study, container, false);
        setListLevel();
        return mView;
    }

    private void setListLevel() {
        mHomeActivity = (HomeActivity) getActivity();
        mDialog = new ProgressDialog(mHomeActivity);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));

        mListLevel = new ArrayList<>();

        mDialog.show();
        APIServer.api.getLevel().enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(Call<List<Level>> call, Response<List<Level>> response) {
                for (Level item : response.body()) {
                    mListLevel.add(item);
                }
                mDialog.dismiss();
                mappingElements();
            }

            @Override
            public void onFailure(Call<List<Level>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mappingElements() {
        mRecyclerView = mView.findViewById(R.id.fs_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mHomeActivity, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mHomeActivity, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        adapter = new CustomeRecyclerStudyAdapter(mListLevel, new CustomeRecyclerStudyAdapter.handlerOnclickItemLevel() {
            @Override
            public void OnclickItemLevl(Level level) {
                toActivitySendData(PartStudyActivity.class,level);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void toActivitySendData(Class<?> to,Level level) {
        Intent intent = new Intent(mHomeActivity,to);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstSendData.LEVEL_ON_CLICK,level);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}