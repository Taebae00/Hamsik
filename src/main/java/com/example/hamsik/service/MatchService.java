package com.example.hamsik.service;

import com.example.hamsik.model.MatchDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchService {

    @Value("${riot.api-key}")
    String apikey;
    String puuidBaseUrl = "https://asia.api.riotgames.com";
    JsonMapper mapper = JsonMapper.builder().build();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String[] getMatchList(String puuid)
            throws IOException, InterruptedException {

        String MatchListUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/"+puuid+"/ids";
        String MatchListJson = requestGet(MatchListUrl);
        String[] matchList = mapper.readValue(MatchListJson, String[].class);

        System.out.println("MatchList: " + matchList.length);


        return matchList;
    }

    public List<MatchDTO> getMatchDetails(String puuid)
            throws IOException, InterruptedException {

        String[] matchIds = getMatchList(puuid);

        List<MatchDTO> matches = new ArrayList<>();

        for (String matchId : matchIds) {

            String url = puuidBaseUrl
                    + "/lol/match/v5/matches/"
                    + matchId;

            String matchJson = requestGet(url);

            MatchDTO match = mapper.readValue(matchJson, MatchDTO.class);

            match.getInfo().getParticipants().sort(
                    Comparator
                            .comparingInt(MatchDTO.ParticipantDTO::getTeamId)
                            .thenComparingInt(MatchDTO.ParticipantDTO::getLaneOrder)
            );


            matches.add(match);
        }

        return matches;
    }

    private String requestGet(String url)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Riot-Token", apikey)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "매치 리스트 조회 실패: HTTP " + response.statusCode()
            );
        }

        return response.body();
    }


}
