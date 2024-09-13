package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private UserRepo userRepo;

    /**
     * @param token
     * @param httpSession
     * @return
     */
    @GetMapping("/varify-email")
    public String verifyEmail(
        @RequestParam("token") String token, HttpSession httpSession,Model model
    ){

        User user=userRepo.findByEmailToken(token).orElse(null);

        if(user != null){

            if(user.getEmailToken().equals(token)){

                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);
                httpSession.setAttribute("message", Message.builder().content("Email is verified. Welcome").type(MessageType.blue).build());
                
                return "user/success_page";
            }
            httpSession.setAttribute("message", Message.builder().content("Email is not verified. Something Wrong").type(MessageType.red).build());

            return "user/error_page";
        }
        httpSession.setAttribute("message", Message.builder().content("Email is not verified. Something Wrong").type(MessageType.red).build());

        return "user/error_page";
    }
}
