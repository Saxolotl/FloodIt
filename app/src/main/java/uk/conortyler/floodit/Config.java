package uk.conortyler.floodit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by conor on 12/02/2018.
 * FloodIt App Programming Assignment 2
 */

public class Config implements Parcelable{

    private int height;
    private int width;
    private int clrCount;
    private int maxRound;
    private String userName;

    /**
     * Constructor for Config to set values
     * @param height Desired value for height
     * @param width Desired value for width
     * @param clrCount Desired value for clrCount
     * @param maxRound Desired value for maxRound
     * @param userName Desired value for userName
     */

    public Config(int height, int width, int clrCount, int maxRound, String userName){
        this.height = height;
        this.width = width;
        this.clrCount = clrCount;
        this.maxRound = maxRound;
        this.userName = userName;
    }

    /**
     * Setter to modify the height
     * @param height The desired height
     */

    public void setHeight(int height){
        this.height = height;
    }

    /**
     * Setter to modify the width
     * @param width The desired width
     */

    public void setWidth(int width){
        this.width = width;
    }

    /**
     * Setter to modify the colour count
     * @param clrCount The desired colour count
     */

    public void setClrCount(int clrCount){
        this.clrCount = clrCount;
    }

    /**
     * Setter to modify the max Round
     * @param maxRound The desired max round
     */

    public void setMaxRound(int maxRound){
        this.maxRound = maxRound;
    }

    /**
     * Setter to modify the user name
     * @param userName The desired user name
     */

    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Getter to return the height
     * @return Current value of height
     */

    public int getHeight(){
        return height;
    }

    /**
     * Getter to return the width
     * @return Current value of width
     */

    public int getWidth(){
        return width;
    }

    /**
     * Getter to return the colour count
     * @return Current value of colour count
     */

    public int getClrCount(){
        return clrCount;
    }

    /**
     * Getter to return the max round
     * @return Current value of max round
     */

    public int getMaxRound(){
        return maxRound;
    }

    /**
     * Getter to return the Username
     * @return Current value of username
     */

    public String getUserName(){
        return userName;
    }

    public int describeContents(){
        return 0;
    }

    /**
     * Used to write the Config to a parcel for use in Intents
     * @param out
     * @param flags
     */

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(height);
        out.writeInt(width);
        out.writeInt(clrCount);
        out.writeInt(maxRound);
        out.writeString(userName);
    }

    public static final Parcelable.Creator<Config> CREATOR =
            new Parcelable.Creator<Config>(){
                public Config createFromParcel(Parcel in){
                    return new Config(in);
                }

                public Config[] newArray(int size){
                    return new Config[size];
                }
            };

    /**
     * Constructor for Config to accept Parcel as an input. Called when passing Config
     * Through different intents / activities
     * @param in
     */

    public Config(Parcel in){
        height = in.readInt();
        width = in.readInt();
        clrCount = in.readInt();
        maxRound = in.readInt();
        userName = in.readString();
    }

}
