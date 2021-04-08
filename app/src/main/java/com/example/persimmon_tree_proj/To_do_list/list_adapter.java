package com.example.persimmon_tree_proj.To_do_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Juang_juang.R;

import java.util.ArrayList;

//  RecyclerView.Adapter


public class list_adapter extends RecyclerView.Adapter<list_adapter.ViewHolder> {
    ArrayList title;
    ArrayList contents;
    ArrayList date;
    ArrayList writer;
    ArrayList color;
    Context context;

    // Constructor for initialization 
    public list_adapter(Context context, ArrayList title, ArrayList contents, ArrayList date, ArrayList writer,ArrayList color) {
        this.context = context;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.writer = writer;
        this.color = color;
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
        String res_title = (String) title.get(position);
        String res_contents = (String) contents.get(position);
        String res_date = (String) date.get(position);
        String res_writer = (String) writer.get(position);
        String res_style = (String) color.get(position);
        holder.title.setText(res_title);
        holder.contents.setText(res_contents);
        holder.date.setText(res_date);
        holder.writer.setText(res_writer);
        if (res_style.equals("1")) { holder.layout.setBackgroundResource(R.drawable.todo_style1);}
        else if (res_style.equals("2")) { holder.layout.setBackgroundResource(R.drawable.todo_style2);}
        else if (res_style.equals("3")) { holder.layout.setBackgroundResource(R.drawable.todo_style3);}
        else{ holder.layout.setBackgroundResource(R.drawable.todo_style4);}

    }

    @Override
    public int getItemCount() {
        // Returns number of items currently available in Adapter 
        return writer.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView title;
        TextView contents;
        TextView writer;
        TextView date;
        LinearLayout layout;


        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView);
            title = (TextView) view.findViewById(R.id.title);
            contents = (TextView) view.findViewById(R.id.contents);
            writer = (TextView) view.findViewById(R.id.writer);
            date = (TextView) view.findViewById(R.id.date);
            layout = (LinearLayout) view.findViewById(R.id.layout);
        }
    }
}