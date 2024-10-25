package com.scm.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @ExceptionHandler
    @PostMapping("/add")
    public String saveContect(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession httpSession) {

        if (result.hasErrors()) {
            httpSession.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailofLoggedInUser(authentication);
        System.out.println(username);
        User user = userService.getUserByEmail(username);
        // process the contact picture

        // System.out.println(user);
        Contact contact = new Contact();

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setFavorite(contactForm.getFavorite());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setWebSitelink(contactForm.getWebSitelink());
        contact.setUser(user);

        if (contactForm.getPicture() != null && !contactForm.getPicture().isEmpty()) {
            String fileName = UUID.randomUUID().toString();

            String fileUrl = imageService.uplodeImage(contactForm.getPicture(), fileName);
            contact.setPicture(fileUrl);
            contact.setCloudinaryImagePublicId(fileName);
        }

        contactService.save(contact);

        httpSession.setAttribute("message", Message.builder()
                .content("You have save a new Contact")
                .type(MessageType.red)
                .build());
        return "redirect:/user/contacts/add";

    }

    @RequestMapping("/con")
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {

        String username = Helper.getEmailofLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("size", pageContact.getContent().size());
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // @RequestMapping("/sidebar")
   

    @RequestMapping("/search")
    public String searchContacts(@ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        var user = userService.getUserByEmail(Helper.getEmailofLoggedInUser(authentication));

        Page<Contact> pageContact = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEnail(contactSearchForm.getValue(), size, page, sortBy, direction,
                    user);
        } else if (contactSearchForm.getField().equalsIgnoreCase("phoneNumber")) {
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                    direction, user);
        }
        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("pageContact", pageContact);
        return "user/search";
    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {

        session.setAttribute("message",
                Message.builder().content("Contact is Deleted successfully!!").type(MessageType.green).build());
        contactService.delete(contactId);
        return "redirect:/user/contacts/con";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactformView(@PathVariable String contactId, Model model) {

        var contact = contactService.getById(contactId);

        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setFavorite(contact.getFavorite());
        contactForm.setLinkedinLink(contact.getLinkedinLink());
        contactForm.setWebSitelink(contact.getWebSitelink());
        contactForm.setPictureUrl(contact.getPicture());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";

    }

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "/user/update_contact_view";
        }
        var con = contactService.getById(contactId);

        con.setId(contactId);
        con.setName(contactForm.getName());

        con.setEmail(contactForm.getEmail());

        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setDescription(contactForm.getDescription());
        con.setAddress(contactForm.getAddress());
        con.setFavorite(contactForm.getFavorite());
        con.setWebSitelink(contactForm.getWebSitelink());

        con.setLinkedinLink(contactForm.getLinkedinLink());

        // process image

        if (contactForm.getPicture() != null && !contactForm.getPicture().isEmpty()) {
            String fileName = UUID.randomUUID().toString();

            String imageUrl = imageService.uplodeImage(contactForm.getPicture(), fileName);

            con.setPicture(imageUrl);
            con.setCloudinaryImagePublicId(fileName);
            contactForm.setPictureUrl(imageUrl);
        }

        contactForm.setPictureUrl(con.getPicture());
        contactService.update(con);

        model.addAttribute("message", Message.builder().content("Contact Updated.").type(MessageType.blue).build());
        return "redirect:/user/contacts/view/" + contactId;
    }
}