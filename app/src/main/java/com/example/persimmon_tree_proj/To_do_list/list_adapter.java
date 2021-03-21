package com.example.persimmon_tree_proj.To_do_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Juang_juang.R;

import java.util.ArrayList;

// Extends the Adapter class to RecyclerView.Adapter 
// and implement the unimplemented methods

public class list_adapter extends RecyclerView.Adapter<list_adapter.ViewHolder> {
    ArrayList images;
    Context context;

    // Constructor for initialization 
    public list_adapter(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml layout file into View object) 
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item, parent, false);

        // Passing view to ViewHolder 
        list_adapter.ViewHolder viewHolder = new list_adapter.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position 
    @Override
    public void onBindViewHolder(@NonNull list_adapter.ViewHolder holder, int position) {
        // TypeCast Object to int type 
        int res = (int) images.get(position);
        holder.images.setImageResource(res);
    }

    @Override
    public int getItemCount() {
        // Returns number of items currently available in Adapter 
        return images.size();
    }

    // Initializing the Views 
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}