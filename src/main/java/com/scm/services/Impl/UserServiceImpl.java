package com.scm.services.Impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private EmailService emailService;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public User saveUser(User user) {

    String userId = UUID.randomUUID().toString();
    user.setUserId(userId);
    System.out.println(user.getPassword());
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // set the role to the user

    user.setRoleList(List.of(AppConstants.ROLE_USER));
    logger.info(user.getProvider().toString());
    

    String emailToken = UUID.randomUUID().toString();
    user.setEmailToken(emailToken);

    
    String emailLink = Helper.getLinkForEmailVarification(emailToken);

    emailService.sendEmail(user.getEmail(), "Verify Account:Email Contact Manager.", emailLink);

    User savedUser = userRepo.save(user);
    return savedUser;

  }

  @Override
  public Optional<User> getUserById(String id) {

    return userRepo.findById(id);
  }

  @Override
  public Optional<User> updateUser(User user) {

    User user2 = userRepo.findById(user.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    user2.setName(user.getName());
    user2.setEmail(user.getEmail());
    user2.setPassword(user.getPassword());
    user2.setAbout(user.getAbout());
    user2.setPhoneNumebr(user.getPhoneNumebr());
    // user2.setEnabled(user.getEnabled());
    // user2.setEmailVerified(user.getEmailVerified());
    // user2.setProfilePic(user.getProfilePic());
    // user2.setPhoneVerified(user.getPhoneVerified());
    user2.setProvider(user.getProvider());
    user2.setProviderUserId(user.getProviderUserId());

    User save = userRepo.save(user2);

    return Optional.ofNullable(save);
  }

  @Override
  public void deleteUser(String id) {
    User user2 = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userRepo.delete(user2);
  }

  @Override
  public boolean isUserExist(String userId) {
    User user2 = userRepo.findById(userId).orElse(null);
    return user2 != null ? true : false;
  }

  @Override
  public boolean isUserExistByEmail(String email) {
    User user = userRepo.findByEmail(email).orElse(null);
    return user != null ? true : false;
  }

  @Override
  public List<User> getAllUser() {
    return userRepo.findAll();
  }

  @Override
  public User getUserByEmail(String email) {

    return this.userRepo.findByEmail(email).orElse(null);

  }

}
