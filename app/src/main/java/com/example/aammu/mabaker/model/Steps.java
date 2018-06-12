package com.example.aammu.mabaker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Steps implements Parcelable{
    @SerializedName("id")
    private int step_id;
    @SerializedName("shortDescription")
    private String short_descrpition;
    @SerializedName("description")
    private String description;
    @SerializedName("videoURL")
    private String video_URL;
    @SerializedName("thumbnailURL")
    private String thumbNail_URL;

    protected Steps(Parcel in) {
        step_id = in.readInt();
        short_descrpition = in.readString();
        description = in.readString();
        video_URL = in.readString();
        thumbNail_URL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    public int getStep_id() {
        return step_id;
    }

    public void setStep_id(int step_id) {
        this.step_id = step_id;
    }

    public String getShort_descrpition() {
        return short_descrpition;
    }

    public void setShort_descrpition(String short_descrpition) {
        this.short_descrpition = short_descrpition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo_URL() {
        return video_URL;
    }

    public void setVideo_URL(String video_URL) {
        this.video_URL = video_URL;
    }

    public String getThumbNail_URL() {
        return thumbNail_URL;
    }

    public void setThumbNail_URL(String thumbNail_URL) {
        this.thumbNail_URL = thumbNail_URL;
    }

    public Steps(int step_id, String short_descrpition, String description, String video_URL, String thumbNail_URL) {
        this.step_id = step_id;
        this.short_descrpition = short_descrpition;
        this.description = description;
        this.video_URL = video_URL;
        this.thumbNail_URL = thumbNail_URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getStep_id());
        dest.writeString(getShort_descrpition());
        dest.writeString(getDescription());
        dest.writeString(getThumbNail_URL());
        dest.writeString(getVideo_URL());
    }
}
