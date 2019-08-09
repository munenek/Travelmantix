package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.MaskTransformation;

//will send data to recylerview through the viewholder
//viewholders get cached to make the view cleaner
public class DealAdapter extends  RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    Transformation transformation;
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabseReference;
    private ChildEventListener mChildListener;

    private ImageView imageDeal;

    public DealAdapter(){
        FireBaseUtil.openFbReference("traveldeals",null);

        mFirebaseDatabase=FireBaseUtil.mFirebaseDatabase;
        mDatabseReference=FireBaseUtil.mDatabaseReference;
        deals=FireBaseUtil.mDeals;
        mChildListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td=dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ",td.getTitle());
                //save the push id so we can easily read this data later
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View itemView= LayoutInflater.from(context).inflate(R.layout.rv_row,parent,false);
        transformation = new MaskTransformation(context, R.drawable.rounded_transformation);
        return new DealViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
TravelDeal deal=deals.get(position);
    holder.bind(deal);
    }


    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;


        public  DealViewHolder(View itemView){
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tvtitle);
            tvDescription=itemView.findViewById(R.id.tvdescription);
            tvPrice=itemView.findViewById(R.id.tvprice);
            imageDeal=itemView.findViewById(R.id.imagedeals);
            itemView.setOnClickListener(this);
        }


        public void bind(TravelDeal deal){

            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());

            showImage(deal.getImageUrl());

        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            Log.d("click",String.valueOf(position));
            TravelDeal selectedDeal=deals.get(position);
            if(FireBaseUtil.isAdmin==true){
            Intent intent=new Intent(view.getContext(), AdminActivity.class);
            intent.putExtra("Deal",selectedDeal);
            view.getContext().startActivity(intent);}
            else {Intent intent=new Intent(view.getContext(), UserActivity.class);
                intent.putExtra("Deal",selectedDeal);
                view.getContext().startActivity(intent);}
        }

        private void showImage(String url){
            if(url !=null&&url.isEmpty()==false){



                Picasso.get().load(url).resize(600,600).centerCrop().transform(transformation).into(imageDeal);
            }

        }
    }
}
