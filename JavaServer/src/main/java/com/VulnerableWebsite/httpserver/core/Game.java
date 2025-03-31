package com.VulnerableWebsite.httpserver.core;

import java.sql.Timestamp;

public class Game {
    // private int id;
    private String title;
    /* private int releaseYear;
    private String platform;
    private String genre;
    private String developer;
    private double rating;
    private Timestamp createdAt; */

    public Game(/*int id, */String title/*, int releaseYear, String platform, String genre, String developer, double rating, Timestamp createdAt*/) {
        //this.id = id;
        this.title = title;
        /*this.releaseYear = releaseYear;
        this.platform = platform;
        this.genre = genre;
        this.developer = developer;
        this.rating = rating;
        this.createdAt = createdAt;*/
    }

    // public int getId() { return id; }
    public String getTitle() { return title; }
    /*public int getReleaseYear() { return releaseYear; }
    public String getPlatform() { return platform; }
    public String getGenre() { return genre; }
    public String getDeveloper() { return developer; }
    public double getRating() { return rating; }
    public Timestamp getCreatedAt() { return createdAt; }*/
}

