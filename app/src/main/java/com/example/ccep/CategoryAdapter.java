package com.example.ccep;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {


        holder.setData(categoryModelList.get(position).getName(),categoryModelList.get(position).getSets());
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView title;


        public Viewholder(View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
        }

        private void setData(final String title, final int sets){

            this.title.setText(title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent setIntent = new Intent(itemView.getContext(),SetsActivity.class);
                    setIntent.putExtra("title",title);
                    setIntent.putExtra("sets",sets);
                    itemView.getContext().startActivity(setIntent);
                }
            });
        }
    }

}
