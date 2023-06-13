package com.proje.nediyoki;

public class Messages {
    public String message;
    public String gonderuid;
    public String aluid;
    public Boolean ben;

    Messages(){

    }

    Messages(String message, String gonderuid, String aluid){
        this.message = message;
        this.aluid = aluid;
        this.gonderuid = gonderuid;
    }
}
