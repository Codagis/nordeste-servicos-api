package com.ordem.servico.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/live")
public class LiveController {

    @PostMapping("/teste")
    public String login() {
        return "API está no ar";
    }
}
