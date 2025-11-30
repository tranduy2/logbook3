package com.example.contactdatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper db;
    List<Contact> contactList;
    ContactAdapter adapter;
    FloatingActionButton fabAdd;

    private int selectedAvatarId = android.R.drawable.ic_menu_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        contactList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_contacts);
        fabAdd = findViewById(R.id.fab_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (db.getAllData().getCount() == 0) {
            db.insertData("Duy", "0382611031", "duy@email.com", R.drawable.kafka);
            db.insertData("Dong", "0382611032", "manh@email.com", R.drawable.suisei);
        }

        loadContacts();
        adapter = new ContactAdapter(this, contactList, new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                showDetailDialog(contact);
            }
        });
        recyclerView.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });
    }

    private void showDetailDialog(Contact contact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_contact_detail, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        ImageView imgAvatar = view.findViewById(R.id.imgDetailAvatar);
        TextView tvName = view.findViewById(R.id.tvDetailName);
        TextView tvPhone = view.findViewById(R.id.tvDetailPhone);
        TextView tvEmail = view.findViewById(R.id.tvDetailEmail);
        Button btnClose = view.findViewById(R.id.btnClose);

        imgAvatar.setImageResource(contact.getAvatarId());
        tvName.setText(contact.getName());
        tvPhone.setText("Phone: " + contact.getPhone());
        tvEmail.setText("Email: " + contact.getEmail());

        // Nút đóng dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    private void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_contact, null);
        builder.setView(view);

        final EditText etName = view.findViewById(R.id.etName);
        final EditText etPhone = view.findViewById(R.id.etPhone);
        final EditText etEmail = view.findViewById(R.id.etEmail);

        final ImageView img1 = view.findViewById(R.id.imgOption1);
        final ImageView img2 = view.findViewById(R.id.imgOption2);
        final ImageView img3 = view.findViewById(R.id.imgOption3);

        img1.setImageResource(android.R.drawable.ic_menu_gallery);
        img2.setImageResource(R.drawable.kafka);
        img3.setImageResource(R.drawable.suisei);

        selectedAvatarId = android.R.drawable.ic_menu_gallery;
        img1.setBackgroundColor(Color.LTGRAY);
        img2.setBackgroundColor(Color.TRANSPARENT);
        img3.setBackgroundColor(Color.TRANSPARENT);

        img1.setOnClickListener(v -> {
            selectedAvatarId = android.R.drawable.ic_menu_gallery;
            img1.setBackgroundColor(Color.LTGRAY);
            img2.setBackgroundColor(Color.TRANSPARENT);
            img3.setBackgroundColor(Color.TRANSPARENT);
        });

        img2.setOnClickListener(v -> {
            selectedAvatarId = R.drawable.kafka;
            img1.setBackgroundColor(Color.TRANSPARENT);
            img2.setBackgroundColor(Color.LTGRAY);
            img3.setBackgroundColor(Color.TRANSPARENT);
        });

        img3.setOnClickListener(v -> {
            selectedAvatarId = R.drawable.suisei;
            img1.setBackgroundColor(Color.TRANSPARENT);
            img2.setBackgroundColor(Color.TRANSPARENT);
            img3.setBackgroundColor(Color.LTGRAY);
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (!name.isEmpty()) {
                    boolean isInserted = db.insertData(name, phone, email, selectedAvatarId);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Contact Added", Toast.LENGTH_SHORT).show();
                        contactList.clear();
                        loadContacts();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Name required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void loadContacts() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) return;
        while (res.moveToNext()) {
            contactList.add(new Contact(res.getInt(0), res.getString(1), res.getString(2), res.getString(3), res.getInt(4)));
        }
    }
}