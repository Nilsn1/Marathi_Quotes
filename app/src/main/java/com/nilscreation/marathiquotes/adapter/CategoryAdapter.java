package com.nilscreation.marathiquotes.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.nilscreation.marathiquotes.Quote;
import com.nilscreation.marathiquotes.R;
import com.nilscreation.marathiquotes.fragment.MainFragment;

import java.util.ArrayList;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<String> categorylist;
    FragmentActivity activity;
    Random r = new Random();
    String finalCategory;

    public CategoryAdapter(Context context, ArrayList<String> categorylist, FragmentActivity activity) {
        this.context = context;
        this.categorylist = categorylist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
        holder.cardview.startAnimation(animation);

        String category = categorylist.get(position);
        finalCategory = Quote.quote(category);
        holder.txtCategory.setText(finalCategory);
        int[] colors = {
                ContextCompat.getColor(context, R.color.cat1),
                ContextCompat.getColor(context, R.color.cat2),
                ContextCompat.getColor(context, R.color.cat3),
                ContextCompat.getColor(context, R.color.cat4),
                ContextCompat.getColor(context, R.color.cat5),
                ContextCompat.getColor(context, R.color.cat6),
                ContextCompat.getColor(context, R.color.cat7)};

        int colorIndex = position % colors.length;
        int backgroundColor = colors[colorIndex];

        holder.txtCategory.setBackgroundColor(backgroundColor);

        holder.txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("Category", category);

                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);
                transaction.replace(R.id.mainContainer, mainFragment);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        LinearLayout layoutCategory;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            cardview = itemView.findViewById(R.id.cardview);
            layoutCategory = itemView.findViewById(R.id.layoutCategory);
        }
    }

}
