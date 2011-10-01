/* Copyright 2011 Mathieu Perreault and Simon Pouliot */

package ca.mcgill.localnose;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class SocialProfile {

	@Persistent
    private User author;

    @Persistent
    private String fullName;

    @Persistent
    private String ulat;

    public String getUlat() {
		return ulat;
	}

	public void setUlat(String ulat) {
		this.ulat = ulat;
	}

	public String getUlon() {
		return ulon;
	}

	public void setUlon(String ulon) {
		this.ulon = ulon;
	}

	@Persistent
    private String ulon;

    @PrimaryKey
    @Persistent
    private String email;

    @Persistent
    private List<Question> questions;

    public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	@Persistent
    private Date date;

    public SocialProfile(User author, Date date, String email, String ulat, String ulon) {
        this.author = author;
        this.date = date;
        this.email = email;
        this.ulat = ulat;
        this.ulon = ulon;
    }

    public User getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		String result = "Social Profile for " + this.email + " " + this.ulat + " " + this.ulon + "\nList of Questions:";
		for (Question q: this.questions) {
			result += "Q: " + q.toString() + "\n";
		}
		return result;

	}
}