package com.example.hamsik.service;

import com.example.hamsik.model.AccountDTO;
import com.example.hamsik.model.LeagueEntryDTO;
import com.example.hamsik.model.SummonerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class AccountService {

    @Value("${riot.api-key}")
    String apikey;
    String puuidBaseUrl = "https://asia.api.riotgames.com";
    String LSUrl = "https://kr.api.riotgames.com";

    JsonMapper mapper = JsonMapper.builder().build();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getUserInfo(String gameName, String tagLine)
            throws IOException, InterruptedException {

        String puuidUrl = puuidBaseUrl + "/riot/account/v1/accounts/by-riot-id/" + encodePath(gameName) + "/" + encodePath(tagLine);

        String accountJson = requestGet(puuidUrl);
        AccountDTO account = mapper.readValue(accountJson, AccountDTO.class);
        return account.getPuuid();
    }

    public LeagueEntryDTO[] getLeague(String puuid) throws IOException, InterruptedException {

        String leagueUrl = LSUrl + "/lol/league/v4/entries/by-puuid/" + puuid;
        String leagueJson = requestGet(leagueUrl);
        System.out.println("leagueJson: " + leagueJson);
        return mapper.readValue(leagueJson, LeagueEntryDTO[].class);
    }

    public SummonerDTO getSummoner(String puuid) throws IOException, InterruptedException {

        String summonerUrl = LSUrl + "/lol/summoner/v4/summoners/by-puuid/" + puuid;
        String summonerJson = requestGet(summonerUrl);
        System.out.println("프로필: " + summonerJson);
        return mapper.readValue(summonerJson, SummonerDTO.class);
    }

    public String encodePath(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    public String getVersion() throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://ddragon.leagueoflegends.com/api/versions.json"
                ))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Data Dragon 버전 조회 실패");
        }

        String[] versions = mapper.readValue(response.body(), String[].class);

        if (versions.length == 0) {
            throw new IllegalStateException("Data Dragon 버전 목록이 비어 있습니다.");
        }

        return versions[0];
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
                    "라이엇 조회 실패: HTTP " + response.statusCode()
            );
        }

        return response.body();
    }
}
