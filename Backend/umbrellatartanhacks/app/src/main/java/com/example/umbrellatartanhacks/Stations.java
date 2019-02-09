package com.example.umbrellatartanhacks;

/**
 * Represents an item in a ToDo list
 */
public class Stations {

    /**
     * Device Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;


    /**
     * location
     */
    @com.google.gson.annotations.SerializedName("loc")
    private String mLoc;

    /**
     * Number of Large Umbrellas
     */
    @com.google.gson.annotations.SerializedName("numLarge")
    private String mnumL;

    /**
     * Number of Small Umbrellas
     */
    @com.google.gson.annotations.SerializedName("numSmall")
    private String mnumS;

    /**
     * Large Ids
     */
    @com.google.gson.annotations.SerializedName("largeIds")
    private String mlargeIds;

    /**
     * Large Ids
     */
    @com.google.gson.annotations.SerializedName("smallIds")
    private String msmallIds;

    /**
     * Devices constructor
     */
    public Stations() {

    }

    @Override
    public String toString() {
        return getId();
    }

    public Stations(String loc, String id) {
        this.setLoc(loc);
        this.setId(id);
    }


    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    public String getNumLarge() {return mnumL;}

    public void setNumLarge(String numL) {mnumL = numL;}

    public String getNumSmall() {return mnumS;}

    public void setNumSmall(String numS) {mnumS = numS;}

    public String getLargeIds() {return mlargeIds;}

    public void setLargeIds(String largeIds) {mlargeIds = largeIds;}

    public String getSmallIds() {return msmallIds;}

    public void setSmallIds(String smallIds) {msmallIds = smallIds;}

    public void setLoc(String loc) {mLoc = loc;}
    public String getLoc() {return mLoc;}

    @Override
    public boolean equals(Object o) {
        return o instanceof Stations && ((Stations) o).mId == mId;
    }
}