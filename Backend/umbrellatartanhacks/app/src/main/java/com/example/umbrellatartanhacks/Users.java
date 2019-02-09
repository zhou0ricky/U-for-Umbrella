package com.example.umbrellatartanhacks;

/**
 * Represents an item in a ToDo list
 */
public class Users {

    /**
     * User Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Users credit card number
     */
    @com.google.gson.annotations.SerializedName("cardNumber")
    private String mCardNumber;

    /**
     * Users last used time
     */
    @com.google.gson.annotations.SerializedName("lastUsed")
    private String mLastUsed;

    /**
     * Users rent time
     */
    @com.google.gson.annotations.SerializedName("rentTime")
    private String mRentTime;

    /**
     * Users rent location
     */
    @com.google.gson.annotations.SerializedName("rentLoc")
    private String mRentLoc;

    /**
     * Users rent status
     */
    @com.google.gson.annotations.SerializedName("inUse")
    private String mInUse;

    /**
     * Users constructor
     */
    public Users() {

    }

    /*@Override
    public String toString() {
        return getId() + ": " + getName();
    }*/

    /**
     * Initializes a new ToDoItem
     *
     * @param text
     *            The item text
     * @param id
     *            The item id
     */
    /*
    public Users(String text, String id) {
        this.setName(text);
        this.setId(id);
    }
    */


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
    public String getUse() {
        return mInUse;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setUse(String Use) {
        mInUse = Use;
    }

    public String getCard() {return mCardNumber;}

    public void setCard(String cardNumber) {mCardNumber = cardNumber;}

    public String getLastUsed() {return mLastUsed;}

    public void setLastUsed(String lastUsed) {mLastUsed = lastUsed;}

    public String getRentTime() {return mRentTime;}

    public void setRentTime(String rentTime) {mRentTime = rentTime;}

    public String getRentLoc() {return mRentLoc;}

    public void setRentLoc(String rentLoc) {mRentLoc = rentLoc;}

    @Override
    public boolean equals(Object o) {
        return o instanceof Users && ((Users) o).mId == mId;
    }
}