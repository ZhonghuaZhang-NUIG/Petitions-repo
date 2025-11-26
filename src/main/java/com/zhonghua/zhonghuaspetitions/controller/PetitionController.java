package com.zhonghua.zhonghuaspetitions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.zhonghua.zhonghuaspetitions.model.Petition;
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

    @GetMapping("/petitions/create")
    public String createForm() {
        return "create";
    }

    @PostMapping("/petitions/create")
    public String createSubmit(@RequestParam String title,
            @RequestParam String description,
            @RequestParam String author) {
        petitionService.create(title, description, author);
        return "redirect:/petitions";
    }

    @GetMapping("/petitions/search")
    public String searchForm() {
        return "search";
    }

    @GetMapping("/petitions/search/results")
    public String searchResults(@RequestParam(name = "q", required = false) String q, Model model) {
        List<Petition> results = petitionService.searchByTitle(q == null ? "" : q);
        model.addAttribute("results", results);
        model.addAttribute("q", q);
        return "searchResults";
    }

    @GetMapping("/petitions/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Petition p = petitionService.findById(id).orElse(null);
        model.addAttribute("petition", p);
        return "detail";
    }

    @PostMapping("/petitions/{id}/sign")
    public String sign(@PathVariable Long id,
            @RequestParam String name,
            @RequestParam String email) {
        petitionService.sign(id, name, email);
        return "redirect:/petitions/" + id;
    }

}
