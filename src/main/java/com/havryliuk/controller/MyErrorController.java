package com.havryliuk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@Controller
@RequestMapping("/error")
public class MyErrorController implements ErrorController {

    @GetMapping
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest request, Exception ex, ModelAndView modelAndView) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            modelAndView.addObject("statusCode", statusCode);
            log.error("statusCode - {}", statusCode);
        }
        String errorRequestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
        if (errorRequestUri != null) {
            String url = request.getRequestURL().toString();
            url = url.substring(0, url.lastIndexOf("/"));
            url += errorRequestUri;
            modelAndView.addObject("url", url);
            log.error("Request: {} raised {}", url, ex);
        }

        modelAndView.setViewName("error");
        return modelAndView;
    }
}
