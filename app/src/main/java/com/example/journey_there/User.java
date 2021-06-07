package com.example.journey_there;

import java.io.Serializable;

public class User implements Serializable {
    private int favorite[];

    public User(){
        favorite = new int[9];
    }

    public void favorite_up(int index){
        favorite[index] += 1;
    }

    public int[] getFavorite(){
        return favorite;
    }
}
