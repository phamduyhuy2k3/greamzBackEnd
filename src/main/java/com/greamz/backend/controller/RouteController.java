package com.greamz.backend.controller;

import com.greamz.backend.service.GameCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequiredArgsConstructor
public class RouteController {
    private final GameCategoryService service;
    @GetMapping("/search")
    public String searchPage(
            @RequestParam("s") String searchText,
            Model model,
            @RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
            @RequestParam(required = false, name = "pageSize", defaultValue = "8") Integer pageSize){

        model.addAttribute("searchText",searchText);
        model.addAttribute("searchPage",true);
        return "filter";


    }
}
