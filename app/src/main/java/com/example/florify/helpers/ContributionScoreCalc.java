package com.example.florify.helpers;

public class ContributionScoreCalc {
    public static int calculateScore(int likes, int views, int postsNo) {
        return postsNo * likes + views * 5;
    }
}
