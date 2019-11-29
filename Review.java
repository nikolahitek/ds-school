package com.nikolahitek;

import java.io.Serializable;

public class Review implements Serializable {
    public String index;
    public String courseID;
    public String activityID;
    public Integer points;

    public Review(String index, String courseID, String activityID, Integer points) {
        this.index = index;
        this.courseID = courseID;
        this.activityID = activityID;
        this.points = points;
    }

    @Override
    public String toString() {
        return index + ", " + courseID + " " + activityID + " : " + points;
    }
}
