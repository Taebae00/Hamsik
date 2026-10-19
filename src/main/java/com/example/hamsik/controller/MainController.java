package com.example.hamsik.controller;

import com.example.hamsik.model.AccountDTO;
import com.example.hamsik.model.LeagueEntryDTO;
import com.example.hamsik.model.MatchDTO;
import com.example.hamsik.model.SummonerDTO;
import com.example.hamsik.service.AccountService;
import com.example.hamsik.service.MatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    private final AccountService accountService;
    private final MatchService matchService;

    public MainController(AccountService accountService, MatchService matchService) {
        this.accountService = accountService;
        this.matchService = matchService;
    }

    @GetMapping("/")
    public String index(Model model)
            throws IOException, InterruptedException {

        AccountDTO account = accountService.getAccount("햄스터", "햄식이");
        SummonerDTO summoner = accountService.getSummoner(account.getPuuid());
        LeagueEntryDTO[] League = accountService.getLeague(account.getPuuid());
        String version = accountService.getVersion();
        List<MatchDTO> matches = matchService.getMatchDetails(account.getPuuid());

        model.addAttribute("matchList", matches);
        model.addAttribute("account", account);
        model.addAttribute("summoner", summoner);
        model.addAttribute("version", version);
        model.addAttribute("League", League);

        return "index";
    }
}
