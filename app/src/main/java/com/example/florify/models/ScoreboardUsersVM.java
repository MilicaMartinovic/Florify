package com.example.florify.models;

public class ScoreboardUsersVM implements Comparable<ScoreboardUsersVM>{

    public String id;
    public String imageUrl;
    public String username;
    public int contributionScore;

    public ScoreboardUsersVM(String id, String imageUrl, String username, int contributionScore) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
        this.contributionScore = contributionScore;
    }

    @Override
    public int compareTo(ScoreboardUsersVM o) {
        return this.contributionScore > o.contributionScore ? -1 : 1;
    }
}
