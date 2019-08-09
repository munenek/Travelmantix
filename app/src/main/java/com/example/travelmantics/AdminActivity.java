package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static java.security.AccessController.getContext;

public class AdminActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDAtabaseReference;
    private final static int PICTURE_RESULT = 42;

    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;
    TravelDeal deal;
    ImageView imageView;
    Uri imageUri;
    String profileImageUrl;
    String picName;
    MenuItem saveOption;
    Button btnImage;
Transformation transformation;
    Context context;

   ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);

        mFirebaseDatabase = FireBaseUtil.mFirebaseDatabase;
        mDAtabaseReference = FireBaseUtil.mDatabaseReference;
        txtTitle = findViewById(R.id.txtname);
        txtDescription = findViewById(R.id.description);
        txtPrice = findViewById(R.id.price);
        imageView = findViewById(R.id.detail_image);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        transformation = new MaskTransformation(this, R.drawable.rounded_transformation);


        Intent intent = getIntent();
        //creating a new deal
        TravelDeal deal = (TravelDeal) intent.getParcelableExtra("Deal");
        if (deal == null) {
            deal = new TravelDeal();

        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
        showImage(deal.getImageUrl());
        btnImage = findViewById(R.id.btn_image);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                //pick images that are only stored in the device
                intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "insert Picture"), PICTURE_RESULT);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                SaveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                //return back to ListActivity After saving
                backToList();

                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show();
                //return back to ListActivity After deleting
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);

        if (FireBaseUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            saveOption=menu.findItem(R.id.save_menu);
            saveOption.setVisible(true);
            enableEditText(true);
            findViewById(R.id.btn_image).setEnabled(true);

        } else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditText(false);
            findViewById(R.id.btn_image).setEnabled(false);
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final Uri imageUri = data.getData();
            StorageReference ref = FireBaseUtil.mStorageRef.child(((Uri) imageUri).getLastPathSegment());
            ref.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    saveOption.setVisible(false);
                    btnImage.setEnabled(false);

                }
            }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while
                    (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    profileImageUrl = downloadUrl.toString();
                    picName=taskSnapshot.getStorage().getPath();
                    showImage(profileImageUrl);
                    if (FireBaseUtil.isAdmin) { saveOption.setVisible(true);}
                        btnImage.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);



                }
            });

        }}


    private void SaveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        deal.setImageUrl(profileImageUrl);
        deal.setImageName(picName);

        if (deal.getId() == null) {
            mDAtabaseReference.push().setValue(deal);

        } else {
            mDAtabaseReference.child(deal.getId()).setValue(deal);
        }

    }

    private void deleteDeal() {

        if (deal == null) {
            Toast.makeText(this, "Please Save the Deal before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        mDAtabaseReference.child(deal.getId()).removeValue();

        if(deal.getImageName()!=null && deal.getImageName().isEmpty()==false){

            StorageReference picRef=FireBaseUtil.mStorage.getReference().child(deal.getImageName());
            picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Deletion","Image deleted Successfuly");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Deletion",e.getMessage());
                }
            });
        }

    }

    //back to listActivity after saving
    private void backToList() {
        /**/
        finish();
        /***/


    }

    private void clean() {
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();

    }

    private void enableEditText(boolean isEnabled) {
        txtTitle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);


    }




    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(url).resize(width, width * 2 / 3).centerCrop().transform(transformation). into(imageView);


        }
    }
}
