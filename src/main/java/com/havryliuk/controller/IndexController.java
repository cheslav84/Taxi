package com.havryliuk.controller;

import lombok.extern.slf4j.Slf4j;
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


    @GetMapping({"/", "/index"})
    public ModelAndView index(ModelAndView modelAndView) {
        log.trace("indexPage");
        modelAndView.setViewName("index");
        return modelAndView;
    }


}
