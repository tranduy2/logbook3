package com.example.contactdatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText etName, etPhone, etEmail, etSearch, etUpdatePhone, etUpdateEmail;
    Button btnAdd, btnView, btnSearch, btnDelete, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etSearch = findViewById(R.id.etSearch);
        etUpdatePhone = findViewById(R.id.etUpdatePhone);
        etUpdateEmail = findViewById(R.id.etUpdateEmail);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnSearch = findViewById(R.id.btnSearch);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnAdd.setOnClickListener(v -> addContact());
        btnView.setOnClickListener(v -> viewContacts());
        btnSearch.setOnClickListener(v -> searchContact());
        btnDelete.setOnClickListener(v -> deleteContact());
        btnUpdate.setOnClickListener(v -> updateContact());
    }

    private void addContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and Phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = db.insertData(name, phone, email);
        if (inserted) {
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
            etName.setText("");
            etPhone.setText("");
            etEmail.setText("");
        } else {
            Toast.makeText(this, "Error adding contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewContacts() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error", "No contacts found");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("ID: ").append(res.getString(0)).append("\n");
            buffer.append("Name: ").append(res.getString(1)).append("\n");
            buffer.append("Phone: ").append(res.getString(2)).append("\n");
            buffer.append("Email: ").append(res.getString(3)).append("\n\n");
        }
        showMessage("All Contacts", buffer.toString());
    }

    private void searchContact() {
        String name = etSearch.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor res = db.getDataByName(name);
        if (res.getCount() == 0) {
            showMessage("Not found", "No contacts matching: " + name);
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("ID: ").append(res.getString(0)).append("\n");
            buffer.append("Name: ").append(res.getString(1)).append("\n");
            buffer.append("Phone: ").append(res.getString(2)).append("\n");
            buffer.append("Email: ").append(res.getString(3)).append("\n\n");
        }
        showMessage("Search Result", buffer.toString());
    }

    private void deleteContact() {
        String name = etSearch.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter a name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        int deletedRows = db.deleteDataByName(name);
        if (deletedRows > 0) {
            Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No contact found with that name", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateContact() {
        String name = etSearch.getText().toString().trim();
        String phone = etUpdatePhone.getText().toString().trim();
        String email = etUpdateEmail.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Enter name to update", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = db.updateDataByName(name, phone, email);
        if (updated) {
            Toast.makeText(this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
            etUpdatePhone.setText("");
            etUpdateEmail.setText("");
        } else {
            Toast.makeText(this, "No contact found with that name", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(message)
                .show();
    }
}
