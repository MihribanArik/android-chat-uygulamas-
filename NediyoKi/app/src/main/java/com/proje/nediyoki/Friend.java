package com.proje.nediyoki;

import androidx.annotation.Keep;

@Keep
public class Friend {
    public String id;
    public String uidFirst;
    public String uidSecond;

    public Friend(){

    }

    public Friend(String id, String uidFirst, String uidSecond){
        this.id = id;
        this.uidFirst = uidFirst;
        this.uidSecond = uidSecond;
    }
}
