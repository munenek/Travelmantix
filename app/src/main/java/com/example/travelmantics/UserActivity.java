package com.example.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;


public class UserActivity extends AppCompatActivity {
    TravelDeal deal;
    ImageView destImg;
    TextView comments;
    TextView description;
    Button sendcomment;
    static final String TAG="RATING";
    EditText writecomment;
    private DatabaseReference mDAtabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SmileRating smileRating;
    String rating="none";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_deal);
        mDAtabaseReference = FireBaseUtil.mDatabaseReference;
        destImg=findViewById(R.id.dest_img);
        comments=findViewById(R.id.comments);
        sendcomment=findViewById(R.id.send_comment);
        writecomment=findViewById(R.id.comment_entry);
        smileRating = findViewById(R.id.smile_rating);
        description=findViewById(R.id.description);
        SmileEar smileEar=new SmileEar();
        smileRating.setOnSmileySelectionListener(smileEar);
        sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendcomment.setEnabled(false);
                Toast.makeText(getApplicationContext(),"Your Comment and Rating have been received, thanks",Toast.LENGTH_LONG).show();
                if(writecomment.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(),"Comments must be at least 10 words",Toast.LENGTH_SHORT).show();
                }
                else {
                    deal.setComments("@"+user.getDisplayName()+" Rated "+rating+"\n Says:: "+ writecomment.getText().toString().replace("null","")+"\n\n");
                    saveDeal();}
            }
        });


        Intent intent = getIntent();
        //creating a new deal
        TravelDeal deal = (TravelDeal) intent.getParcelableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();

        }

        this.deal = deal;


        try{
            description.setText(deal.getDescription());

            comments.setText(deal.getComments().replace("*_*","\n").replace("null",""));}
        catch (NullPointerException g){}
        showImage(deal.getImageUrl());
    }




    private void saveDeal() {

        if (deal.getId() == null) {
            mDAtabaseReference.push().setValue(deal);

        } else {
            mDAtabaseReference.child(deal.getId()).setValue(deal);
        }

    }



private class SmileEar implements SmileRating.OnSmileySelectionListener{
    @Override
    public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
        // reselected is false when user selects different smiley that previously selected one
        // true when the same smiley is selected.
        // Except if it first time, then the value will be false.
        switch (smiley) {
            case SmileRating.BAD:
                Log.i(TAG, "Bad");

                rating="2/5";
                break;
            case SmileRating.GOOD:
                Log.i(TAG, "Good");
                rating="4/5";
                break;
            case SmileRating.GREAT:
                Log.i(TAG, "Great");
                rating="5/5";
                break;
            case SmileRating.OKAY:
                Log.i(TAG, "Okay");
                rating="3/5";
                break;
            case SmileRating.TERRIBLE:
                Log.i(TAG, "Terrible");
                rating="1/5";
                break;
        }
    }
}




    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width * 2 / 3).centerCrop().into(destImg);


        }
    }
}
