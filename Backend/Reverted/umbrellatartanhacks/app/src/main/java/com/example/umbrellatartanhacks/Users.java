package com.example.umbrellatartanhacks;

/**
 * Represents an item in a ToDo list
 */
public class Users {

    /**
     * User Name
     */
    @com.google.gson.annotations.SerializedName("name")
    private String mName;

    /**
     * User Id
     */
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

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
    @com.google.gson.annotations.SerializedName("rentLocX")
    private String mRentLocX;
    @com.google.gson.annotations.SerializedName("rentLocY")
    private String mRentLocY;

    /**
     * Users constructor
     */
    public Users() {

    }

    @Override
    public String toString() {
        return getId() + ": " + getName();
    }

    /**
     * Initializes a new ToDoItem
     *
     * @param text
     *            The item text
     * @param id
     *            The item id
     */
    public Users(String text, String id) {
        this.setName(text);
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

    public String getName() {return mName;}

    public void setName(String name) {mName = name;}

    public String getCard() {return mCardNumber;}

    public void setCard(String cardNumber) {mCardNumber = cardNumber;}

    public String getLastUsed() {return mLastUsed;}

    public void setLastUsed(String lastUsed) {mLastUsed = lastUsed;}

    public String getRentTime() {return mRentTime;}

    public void setRentTime(String rentTime) {mRentTime = rentTime;}

    public String getRentLocX() {return mRentLocX;}

    public void setRentLocX(String rentLocX) {mRentLocX = rentLocX;}

    public String getRentLocY() {return mRentLocY;}

    public void setRentLocY(String rentLocY) {mRentLocY = rentLocY;}

    @Override
    public boolean equals(Object o) {
        return o instanceof Users && ((Users) o).mId == mId;
    }
}