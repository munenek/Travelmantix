package com.example.travelmantics;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


//better to use parcelable here for better performance
public class TravelDeal implements Parcelable {

    //use creator class to write a new parcel or a set
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public TravelDeal createFromParcel(Parcel in) {
            return new TravelDeal(in);
        }

        public TravelDeal[] newArray(int size) {
            return new TravelDeal[size];
        }
    };
//variables well store in a parcel



    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;
    private String imageName;
    private String comments;
    private String rating;

    public TravelDeal(){}


    public TravelDeal(  String title, String description, String price, String imageUrl, String imageName,String comments,String rating, String num_ratings) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setPrice(price);
        this.setImageUrl(imageUrl);
        this.setImageName(imageName);
        this.setComments(comments);
        this.setRating(rating);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments +=("*_*"+ comments).trim();
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    // Parcel the variables
    public TravelDeal(Parcel in){
        this.id = in.readString();
        this.title =  in.readString();
        this.description =  in.readString();
        this.price =  in.readString();
        this.imageUrl=in.readString();
        this.imageName=in.readString();
        this.comments=in.readString();
        this.rating=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.price);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageName);
        dest.writeString(this.comments);
        dest.writeString(this.rating);

    }

}


