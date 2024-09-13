package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Controller
public class OAuthAutheticationSuccesHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAutheticationSuccesHandler.class);

    @Autowired
    private UserRepo userRepo;

    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAutheticationSuccesHandler");

        // identified the provider
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        // logger.info(authorizedClientRegistrationId);

        var user = (DefaultOAuth2User) authentication.getPrincipal();


        // logger.info(user.getName());
        // logger.info(user.getName());

        user.getAttributes().forEach((key, value) -> {
            logger.info(key + ":" + value);
        });

        
        // logger.info(user.getAttribute(authorizedClientRegistrationId));
        User user1 = new User();
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        
        
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            // google

            user1.setEmail(user.getAttribute("email").toString());
            user1.setProfilePic(user.getAttribute("picture").toString());
            user1.setName(user.getAttribute("name").toString());
            user1.setProviderUserId(user.getName());
            user1.setAbout("This account is created using google.");
            user1.setProvider(Providers.Google);

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            // github
           
            try {
            String email = user.getAttribute("email").toString();
            user1.setEmail(email);

            } catch (NullPointerException e) {
               String email = user.getAttribute("login").toString() + "@gmail.com";
               user1.setEmail(email);
            }

            String picture = user.getAttribute("avatar_url").toString();
            String name = user.getAttribute("login").toString();
            String providerUserId = user.getName().toString();

            
            user1.setProfilePic(picture);
            user1.setName(name);
            user1.setProviderUserId(providerUserId);
            user1.setAbout("This account is created using github.");
            user1.setProvider(Providers.Github);
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin"))

        {

        } else {

        }

        User user2 = userRepo.findByEmail(user1.getEmail()).orElse(null);
        if (user2 == null) {
            userRepo.save(user1);
            logger.info("User Saved: " + user1.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }

}
