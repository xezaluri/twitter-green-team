package com.m2i.poec.twittergreen.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@Column(name = "iduser")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="author")
	@OrderBy("creationDate DESC")
	private List<Tweet> tweets;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="author")
	@OrderBy("creationDate DESC")
	private List<Retweet> retweets;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;

	@Column(name = "picture")
	private String picture;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public List<Retweet> getRetweets() {
		return retweets;
	}

	public void setRetweets(List<Retweet> retweets) {
		this.retweets = retweets;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password
				+ ", email=" + email + ", picture=" + picture + "]";
	}

	public void addTweet(Tweet tweet) {
		List<Tweet> newTweets = new ArrayList<Tweet>();
		newTweets.add(tweet);
		if (tweets != null) newTweets.addAll(tweets);
		tweets = newTweets;
	}
	
	public void addReTweet(Retweet retweet) {
		List<Retweet> newReTweets = new ArrayList<Retweet>();
		newReTweets.add(retweet);
		if (newReTweets != null ) newReTweets.addAll(retweets);
		retweets = newReTweets;		
	}
}
