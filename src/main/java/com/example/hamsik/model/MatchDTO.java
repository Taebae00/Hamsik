package com.example.hamsik.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDTO {

    private InfoDTO info;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoDTO {
        private long gameDuration;
        private int queueId;
        private List<ParticipantDTO> participants;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParticipantDTO {

        private String puuid;
        private String riotIdGameName;
        private String riotIdTagline;

        private int teamId;
        private String teamPosition;

        private int championId;
        private String championName;
        private boolean win;

        private int kills;
        private int deaths;
        private int assists;

        private int totalDamageDealtToChampions;

        private int summoner1Id;
        private int summoner2Id;

        private int item0;
        private int item1;
        private int item2;
        private int item3;
        private int item4;
        private int item5;
        private int item6;

        public int getLaneOrder() {
            if (teamPosition == null) {
                return 5;
            }

            return switch (teamPosition) {
                case "TOP" -> 0;
                case "JUNGLE" -> 1;
                case "MIDDLE" -> 2;
                case "BOTTOM" -> 3;
                case "UTILITY" -> 4;
                default -> 5;
            };
        }

        public String getLaneName() {
            if (teamPosition == null) {
                return "-";
            }

            return switch (teamPosition) {
                case "TOP" -> "탑";
                case "JUNGLE" -> "정글";
                case "MIDDLE" -> "미드";
                case "BOTTOM" -> "원딜";
                case "UTILITY" -> "서폿";
                default -> "-";
            };
        }

        public int[] getItems() {
            return new int[]{
                    item0, item1, item2, item3,
                    item4, item5, item6
            };
        }
    }
}