package com.havryliuk.controller;

import com.havryliuk.model.Tariffs;
import com.havryliuk.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class IndexController {

    private final PaymentService paymentService;

    @Autowired
    public IndexController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping({"/", "/index"})
    public ModelAndView index(ModelAndView modelAndView) {
        log.trace("indexPage");
        Iterable<Tariffs> tariffs = paymentService.findAll();
        modelAndView.addObject("tariffs", tariffs);
        modelAndView.addObject("activePage", "index");
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
