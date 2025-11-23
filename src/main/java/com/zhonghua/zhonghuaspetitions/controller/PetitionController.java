package com.zhonghua.zhonghuaspetitions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zhonghua.zhonghuaspetitions.service.PetitionService;

import java.util.List;

@Controller
public class PetitionController {

    private final PetitionService petitionService;

    @Autowired
    public PetitionController(PetitionService petitionService) {
        this.petitionService = petitionService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/petitions";
    }

    @GetMapping("/petitions")
    public String list(Model model) {
        model.addAttribute("petitions", petitionService.findAll());
        return "list";
    }

}
