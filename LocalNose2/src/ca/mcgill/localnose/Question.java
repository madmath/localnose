/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

package ca.mcgill.localnose;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Question {

	@Persistent
    private final String question;

    @Persistent
    private String lat;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String lon;

	@Persistent
    private final Date date;

    public Question(String question, Date date) {
    	this.question = question;
    	this.date = date;
    }

    public String getQuestion () {
    	return this.question;
    }

    public void setLatitude(String lat){
    	this.lat = lat;
    }

    public void setLongitude(String lon) {

    	this.lon = lon;
    }

    @Override
	public String toString() {
    	return this.question + " " +this.lat + " " + this.lon;
    }
}