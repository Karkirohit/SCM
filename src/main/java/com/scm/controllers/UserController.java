package com.scm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger=LoggerFactory.getLogger(UserController.class);


   

@RequestMapping(value = "/dashboard", method={RequestMethod.POST,RequestMethod.GET})
    public String userDashboard(){
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile", method={RequestMethod.POST,RequestMethod.GET})
    public String userProfile( ){
        return "user/profile";
    }

}
