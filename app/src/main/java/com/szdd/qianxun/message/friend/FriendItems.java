package com.szdd.qianxun.message.friend;

import java.util.ArrayList;

public class FriendItems {
    private ArrayList<AnFriendItem> array = new ArrayList<>();

    public FriendItems() {
    }

    public FriendItems(ArrayList<AnFriendItem> array) {
        this.array = array;
    }

    public ArrayList<AnFriendItem> getArray() {
        return array;
    }

    public void setArray(ArrayList<AnFriendItem> array) {
        this.array = array;
    }
}
