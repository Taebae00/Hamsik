package com.example.hamsik.model;

import lombok.Data;

@Data
public class LeagueEntryDTO {

    String leagueId;
    String puuid;
    String queueType;
    String tier;
    String rank;
    int leaguePoints;
    int wins;
    int losses;
    boolean hotStreak;
    boolean veteran;
    boolean freshBlood;
    boolean inactive;
    MiniSeriesDTO miniSeries;
}
