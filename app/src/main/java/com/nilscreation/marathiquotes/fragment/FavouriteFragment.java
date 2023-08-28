package com.nilscreation.marathiquotes.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nilscreation.marathiquotes.R;
import com.nilscreation.marathiquotes.adapter.FavouriteAdapter;
import com.nilscreation.marathiquotes.adapter.QuoteAdapter;
import com.nilscreation.marathiquotes.model.QuoteModel;
import com.nilscreation.marathiquotes.service.MyDBHelper;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    RecyclerView recyclerView;
    List<String> quotelist;

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        quotelist = new ArrayList<>();

        return view;
    }

    private void fetchData() {
        MyDBHelper myDBHelper = new MyDBHelper(getContext());
        quotelist = myDBHelper.readData();

        FavouriteAdapter adapter = new FavouriteAdapter(getContext(), quotelist, getActivity());
//                    recyclerView.setAdapter(adapter);
//            AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-9137303962163689/3884272678", adapter,
//                    "small").adItemInterval(3).build();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        quotelist.clear();
        fetchData();
    }
}