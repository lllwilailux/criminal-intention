package com.augmentis.ayp.crimin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Wilailux on 7/18/2016.
 */
public class Crime {

    private UUID id;
    private String title;
    private Date crimeDate;
    private boolean solved;


    public Crime(){
        id = UUID.randomUUID();
        crimeDate = new Date();
//
//        SimpleDateFormat sdf = new SimpleDateFormat( "d MMMM yyyy" );
//        sdf.format(crimeDate);
    }
//
//    public String getFormattedDate(String sdf){
//        return sdf;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCrimeDate() {
        return crimeDate;
    }


    public void setCrimeDate(Date crimeDate) {

        this.crimeDate = crimeDate;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UUID=").append(id);
        builder.append(",Title=").append(title);
        builder.append(",Crime Date=").append(crimeDate);
        builder.append(",solves=").append(solved);
        return builder.toString();
    }
}
