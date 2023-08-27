package com.nilscreation.marathiquotes.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nilscreation.marathiquotes.EditActivity;
import com.nilscreation.marathiquotes.MainActivity;
import com.nilscreation.marathiquotes.R;
import com.nilscreation.marathiquotes.adapter.QuoteAdapter;
import com.nilscreation.marathiquotes.model.QuoteModel;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    TextView title;
    private RecyclerView recyclerView;
    private List<QuoteModel> factslist;
    Context context;
    String category;
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        title = view.findViewById(R.id.title);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        title = view.findViewById(R.id.title);
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                startActivity(intent);
                return false;
            }
        });

        factslist = new ArrayList<>();
        fetchData();

        Bundle bundle = getArguments();
        if (bundle != null) {
            category = bundle.getString("Category");
            title.setText(category);
        } else {
            category = "All";
            title.setText(getString(R.string.app_name));
        }

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        return view;
    }

    private void fetchData() {

        QuoteAdapter adapter = new QuoteAdapter(context, factslist, getActivity());
        recyclerView.setAdapter(adapter);
//        AdmobNativeAdAdapter admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with("ca-app-pub-9137303962163689/7340301951", adapter, "medium").adItemInterval(10).build();
//        recyclerView.setAdapter(admobNativeAdAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MarathiQuotes DB");
        databaseReference.child("Quotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                factslist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuoteModel quoteModel = dataSnapshot.getValue(QuoteModel.class);

                    String mcategory = quoteModel.getCategory();
                    if (mcategory.equals(category)) {
                        factslist.add(quoteModel);
                    } else if (category.equals("All")) {
                        factslist.add(quoteModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}