package com.example.hamsik.controller;

import com.example.hamsik.model.AccountDTO;
import com.example.hamsik.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Controller
public class MainController {

    private final AccountService accountService;

    public MainController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String index(Model model)
            throws IOException, InterruptedException {

        String result = accountService.getUserInfo("반대로말하고", "1212");


        System.out.println("라이엇 응답: " + result);

        return "index";
    }
}
