package com.example.englishlearn.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearn.R;
import com.example.englishlearn.activities.CreateTheTestActivity;
import com.example.englishlearn.activities.DoWorkTestActivity;
import com.example.englishlearn.activities.HomeActivity;
import com.example.englishlearn.api.APIServer;
import com.example.englishlearn.customes.CustomeRecyclerTextAdapter;
import com.example.englishlearn.handlerothers.ConstSendData;
import com.example.englishlearn.models.TheTestOfUsers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestOfUserFragment extends Fragment {
    private View mView;
    private EditText txtSearch;
    private Button btnAdd, btnSearch;
    private RecyclerView rcvResult;
    private CustomeRecyclerTextAdapter adapter;
    private List<TheTestOfUsers> mList;
    private ProgressDialog mDialog;
    private HomeActivity mHomeActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_test_of_user, container, false);

        mappingElements();
        eventElements();

        return mView;
    }

    private void mappingElements() {
        mHomeActivity = (HomeActivity) getActivity();
        mList = new ArrayList<>();
        mDialog = new ProgressDialog(mHomeActivity);
        mDialog.setTitle(R.string.title_progess);
        mDialog.setMessage(getResources().getString(R.string.content_progess));
        txtSearch = mView.findViewById(R.id.ftou_txtSearch);
        btnAdd = mView.findViewById(R.id.ftou_btnAdd);
        btnSearch = mView.findViewById(R.id.ftou_btnSearch);

        rcvResult = mView.findViewById(R.id.ftou_recyclerData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mHomeActivity, RecyclerView.VERTICAL, false);
        rcvResult.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(mHomeActivity, DividerItemDecoration.VERTICAL);
        rcvResult.addItemDecoration(itemDecoration);
        adapter = new CustomeRecyclerTextAdapter(mList, new CustomeRecyclerTextAdapter.HandlerOpenTheTest() {
            @Override
            public void handlerOnclickItem(TheTestOfUsers item) {
                openActivity(item);
            }
        });
        rcvResult.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void openActivity(TheTestOfUsers item) {
        Intent intent = new Intent(mHomeActivity, DoWorkTestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstSendData.KEY_SEND_OBJECT_TEST,item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openActivity() {
        Intent intent = new Intent(mHomeActivity, CreateTheTestActivity.class);
        startActivity(intent);
    }

    private void eventElements() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList = new ArrayList<>();
                handlerSearchResult();
            }
        });
    }

    private void handlerSearchResult() {
        String searchText = txtSearch.getText().toString().trim();
        if (searchText.isEmpty()) {
            txtSearch.requestFocus();
            txtSearch.setHintTextColor(getResources().getColor(R.color.red));
            return;
        }
        mDialog.show();
        APIServer.api.searchTest(searchText).enqueue(new Callback<List<TheTestOfUsers>>() {
            @Override
            public void onResponse(Call<List<TheTestOfUsers>> call, Response<List<TheTestOfUsers>> response) {
                if (response.body() == null) {
                    Toast.makeText(mHomeActivity, getString(R.string.Couldnt_you_need), Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    return;
                }

                for (TheTestOfUsers item : response.body()) {
                    mList.add(item);
                }
                resetEditText();
                adapter.setDataList(mList);
                mDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<TheTestOfUsers>> call, Throwable t) {
                mDialog.dismiss();
                Toast.makeText(mHomeActivity, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetEditText() {
        txtSearch.setText("");
        txtSearch.setHintTextColor(getResources().getColor(R.color.gray));
    }
}