package com.example.sih;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

class CropInference implements Parcelable {
    protected CropInference(Parcel in) {
        sowing = in.readString();
        harvestSeason = in.readString();
    }

    public static final Creator<CropInference> CREATOR = new Creator<CropInference>() {
        @Override
        public CropInference createFromParcel(Parcel in) {
            return new CropInference(in);
        }

        @Override
        public CropInference[] newArray(int size) {
            return new CropInference[size];
        }
    };

    public CropInference() {

    }

    public String getSowing() {
        return sowing;
    }

    public void setSowing(String sowing) {
        this.sowing = sowing;
    }

    public String getHarvestSeason() {
        return harvestSeason;
    }

    public void setHarvestSeason(String harvestSeason) {
        this.harvestSeason = harvestSeason;
    }

    @SerializedName("sowing_date")
    String sowing;


    @SerializedName("harvest_season")
    String harvestSeason;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sowing);
        dest.writeString(harvestSeason);
    }
}
