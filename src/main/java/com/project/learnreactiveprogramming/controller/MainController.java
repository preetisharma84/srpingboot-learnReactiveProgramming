package com.project.learnreactiveprogramming.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

//@Controller says that these response values are for looking up the html page
@Controller
public class MainController {

    //@Controller will look for home.html page in the template folder
    @GetMapping("/")
    public Mono<String> handleMain(){
        return Mono.just("home");
    }
}