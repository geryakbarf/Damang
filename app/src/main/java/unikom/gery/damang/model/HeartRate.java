package unikom.gery.damang.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HeartRate implements Parcelable {

    public static final Creator<HeartRate> CREATOR = new Creator<HeartRate>() {
        @Override
        public HeartRate createFromParcel(Parcel source) {
            return new HeartRate(source);
        }

        @Override
        public HeartRate[] newArray(int size) {
            return new HeartRate[size];
        }
    };
    private int averageHeartRate;
    private String date;
    private int currentHeartRate;

    public HeartRate() {

    }

    public HeartRate(int averageHeartRate, String date, int currentHeartRate) {
        this.averageHeartRate = averageHeartRate;
        this.date = date;
        this.currentHeartRate = currentHeartRate;
    }

    protected HeartRate(Parcel in) {
        this.averageHeartRate = in.readInt();
        this.date = in.readString();
        this.currentHeartRate = in.readInt();
    }

    public int getCurrentHeartRate() {
        return currentHeartRate;
    }

    public void setCurrentHeartRate(int currentHeartRate) {
        this.currentHeartRate = currentHeartRate;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.averageHeartRate);
        dest.writeString(this.date);
        dest.writeInt(this.currentHeartRate);
    }

    public void readFromParcel(Parcel source) {
        this.averageHeartRate = source.readInt();
        this.date = source.readString();
        this.currentHeartRate = source.readInt();
    }
}
