package com.nilscreation.marathiquotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nilscreation.marathiquotes.model.DataModel;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    EditText quote;
    ArrayList<String> categorylist;
    String mCategory, mQuote;
    Button submit, clear;
    long itemNumber;
    DataModel dataModel;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        spinnerCategory = findViewById(R.id.spinner);
        quote = findViewById(R.id.quote);
        submit = findViewById(R.id.submit);
        clear = findViewById(R.id.clear);

        reference = FirebaseDatabase.getInstance().getReference("MarathiQuotes DB");

        loadCategory();
        categorylist = new ArrayList<>();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCategory = categorylist.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuote = quote.getText().toString();

                if (mQuote.isEmpty()) {
                    quote.setError("Field Required");
                } else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActivity.this);
                    alertDialog.setTitle("Submit");
                    alertDialog.setMessage("Dou You Really want to Submit?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateData();
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quote.setText("");
            }
        });

    }

    private void loadCategory() {
        reference.child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String category = dataSnapshot.getKey();
                    categorylist.add(category);
                }
                ArrayAdapter adapter = new ArrayAdapter<>(EditActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, categorylist);
                spinnerCategory.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Quotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemNumber = snapshot.getChildrenCount();
                Toast.makeText(EditActivity.this, " " + itemNumber, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData() {
        dataModel = new DataModel(mCategory, mQuote);

        reference.child("Quotes").child(String.valueOf("Q" + (itemNumber + 1))).setValue(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditActivity.this, "Error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}