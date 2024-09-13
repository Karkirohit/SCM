package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    @SuppressWarnings("null")
    public static String getEmailofLoggedInUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {

            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();

            String username = "";

            if (clientId.equalsIgnoreCase("google")) {

                System.out.println("google sign up");
                username = oauth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {

                System.out.println("github signup");
                try {
                    username = oauth2User.getAttribute("email").toString();

                } catch (NullPointerException e) {
                    username = oauth2User.getAttribute("login").toString() + "@gmail.com";

                }

            }

            return username;

        } else {
            return authentication.getName();
        }

    }


    public static String getLinkForEmailVarification(String emailToken){

        String link="http://localhost:8081/auth/varify-email?token="+emailToken;

        return link;

    }
}
