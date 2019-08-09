package com.example.travelmantics;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//CLASS TO DEAL WITH FIREBASE DIRECTLY
public class FireBaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FireBaseUtil fireBaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static ArrayList<TravelDeal> mDeals;
    private static ListActivity caller;
    private static int RC_SIGN_IN = 100;
    public static boolean isAdmin;


//constructor to avoid being instantiated from outside this class

    private FireBaseUtil(){}


    public static void attachListener(){
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public static void detachListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }


    public static void openFbReference(String ref, final ListActivity callerActivity){
        if(fireBaseUtil==null) {
            fireBaseUtil = new FireBaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth=FirebaseAuth.getInstance();
            caller=callerActivity;
            mAuthListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser()==null){


                        FireBaseUtil.signIn();}

                    else {
                        String userid=firebaseAuth.getUid();
                        checkAdmin(userid);
                    }

                }
            };
            connectStorage();

        }

        mDeals = new ArrayList<TravelDeal>();
        mDatabaseReference=mFirebaseDatabase.getReference().child(ref);
    }




    private static void checkAdmin(String uid){
        FireBaseUtil.isAdmin=false;
        DatabaseReference ref=mFirebaseDatabase.getReference().child("administrators").child(uid);

        ChildEventListener listener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FireBaseUtil.isAdmin=true;
                caller.showMenu();
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
        ref.addChildEventListener(listener);
    }


    private static void signIn(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());



// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers).setLogo(R.drawable.ic_tour_logo_text).setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }

    public static void connectStorage(){
        mStorage=FirebaseStorage.getInstance();
        mStorageRef=mStorage.getReference().child("deals_pictures");
    }


}