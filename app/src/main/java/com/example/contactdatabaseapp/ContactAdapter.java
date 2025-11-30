package com.example.contactdatabaseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contactList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }

    public ContactAdapter(Context context, List<Contact> contactList, OnItemClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        holder.tvName.setText(contact.getName());
        holder.tvPhone.setText(contact.getPhone());
        holder.imgAvatar.setImageResource(contact.getAvatarId());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(contact);
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName, tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.image_avatar);
            tvName = itemView.findViewById(R.id.text_name);
            tvPhone = itemView.findViewById(R.id.text_phone);
        }
    }
}