package com.example.cleaningapplication.Model;

import android.net.Uri;

public class Notifcation {
    String From;
    String Message;
    String To;
    String Date;
    String Request;
    String Clicked;
    String FromPicture;
    String Id;

    public String getFromPicture(){return FromPicture;}

    public String getId(){return Id;}

    public String getClicked(){return Clicked;}

    public String getMessage(){return Message;}

    public String getFrom() {
        return From;
    }

    public String getRequest() {
        return Request;
    }

    public String getTo() {
        return To;
    }

    public String getDate() {
        return Date;
    }

}
