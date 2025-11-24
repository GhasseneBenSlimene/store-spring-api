package com.ghassene.store.controllers;

import com.ghassene.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public Message sayHello(){
        return new Message("Hello, World!");
    }
}
