package com.havryliuk.controller;

import com.havryliuk.model.Tariffs;
import com.havryliuk.service.TariffsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class IndexController {

//    @GetMapping("/")
//    public String home() {
//        return "index";
//    }

    private final TariffsService tariffsService;

    @Autowired
    public IndexController(TariffsService tariffsService) {
        this.tariffsService = tariffsService;
    }

    @GetMapping({"/", "/index"})
    public ModelAndView index(ModelAndView modelAndView) {
        log.trace("indexPage");
        Iterable<Tariffs> tariffs = tariffsService.findAll();
        modelAndView.addObject("tariffs", tariffs);
        modelAndView.addObject("activePage", "index");
        modelAndView.setViewName("index");
        return modelAndView;
    }


}
