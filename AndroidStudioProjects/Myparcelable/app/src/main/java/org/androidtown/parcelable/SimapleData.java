package org.androidtown.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MyeongjinLEE on 2017-12-28.
 */

public class SimapleData implements Parcelable {

    int number;
    String message;

    public SimapleData(int number, String message) {
        this.number = number;
        this.message = message;
    }

    public SimapleData(Parcel src){
        number = src.readInt();
        message = src.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public SimapleData createFromParcel(Parcel in) {
            return new SimapleData(in);
        }

        public SimapleData[] newArray(int size) {
            return new SimapleData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeString(message);
    }
}
