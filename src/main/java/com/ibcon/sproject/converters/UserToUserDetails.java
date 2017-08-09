package com.ibcon.sproject.converters;

import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.services.crud.user.UserDetailsImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

//Позволяет использовать роли в шаблонах
@Component
public class UserToUserDetails implements Converter<User, UserDetails> {
    @Override
    public UserDetails convert(User user) {
        //TODO bean?
        UserDetailsImpl userDetails = new UserDetailsImpl();

        if (user != null) {
            userDetails.setUsername(user.getName());
            userDetails.setPassword(user.getEncryptedPassword());
            //TODO работает? -да
            userDetails.setActive(user.isActive());
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            userDetails.setAuthorities(authorities);
        }

        return userDetails;
    }
}