package com.ecommerce.book_store.service.oauth;

import com.ecommerce.book_store.persistent.entity.User;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User implements CustomUser {
    private final User user;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        super(null, attributes, "sub");
        this.user = user;
    }

}
