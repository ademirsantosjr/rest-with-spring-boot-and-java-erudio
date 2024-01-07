package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.controllers.PersonController;
import br.com.erudio.erudioapi.dto.v1.PersonDto;
import br.com.erudio.erudioapi.dto.v2.PersonDtoV2;
import br.com.erudio.erudioapi.exceptions.RequiredObjectNullException;
import br.com.erudio.erudioapi.exceptions.ResourceNotFoundException;
import br.com.erudio.erudioapi.mapper.PersonMapper;
import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.model.User;
import br.com.erudio.erudioapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class.getName());
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding a user by username " + username );
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
    }
}
