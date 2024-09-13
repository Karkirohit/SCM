
package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.PageForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        System.out.println("home controller");
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        System.out.println("about page controller");
        return "about";
    }

    @GetMapping("/services")
    public String servicePage() {
        System.out.println("service page controller");
        return "service";
    }

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
    public String login() {

        // String username = Helper.getEmailofLoggedInUser(authentication);

        // System.out.println(username);

        // User user = userService.getUserByEmail(username);
        // if (!user.isEnabled()) {
        //     String emailToken = UUID.randomUUID().toString();
        //     user.setEmailToken(emailToken);

        //     User savedUser = userRepo.save(user);
        //     String emailLink = Helper.getLinkForEmailVarification(emailToken);

        //     emailService.sendEmail(savedUser.getEmail(), "Verify Account:Email Contact Manager.", emailLink);

        // }
        return "login";
    }

    @GetMapping("/Signup")
    public String signup(Model model) {
        PageForm pageForm = new PageForm();

        model.addAttribute("pageForm", pageForm);

        return new String("register");
    }

    @RequestMapping(value = "/do_register", method = RequestMethod.POST)

    public String processRegister(@Valid @ModelAttribute PageForm pageForm, BindingResult result,
            HttpSession session, Model model) {

        // validation Process
        if (result.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setName(pageForm.getName());
        user.setEmail(pageForm.getEmail());
        user.setPassword(pageForm.getPassword());
        user.setAbout(pageForm.getAbout());
        user.setPhoneNumebr(pageForm.getPhoneNumber());
        user.setProfilePic("/images/profile_4x.jpg");
        user.setEnabled(false);
        Message message = Message.builder().content("Registration Session").type(MessageType.green).build();

        User savedUser = userService.saveUser(user);
        System.out.println(savedUser);
        session.setAttribute("message", message);
        System.out.println("usersaved");
        return "redirect:/Signup";

    }

}
