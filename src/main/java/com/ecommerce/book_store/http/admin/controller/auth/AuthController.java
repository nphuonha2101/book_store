package com.ecommerce.book_store.http.admin.controller.auth;

import com.ecommerce.book_store.http.dto.request.implement.UserRequestDto;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "pages/auth/login";
    }
    @GetMapping("/register")
    public String registerUser() {
        return "pages/auth/register";
    }
    @PostMapping("/register")
    public ModelAndView registerUser(
//                                    @RequestParam("username") String username,
                                     @RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("phone") String phone,
                                     @RequestParam("address") String address) {
        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setUsername(username);
        userRequestDto.setEmail(email);
        userRequestDto.setPassword(password);
        userRequestDto.setPhone(phone);
        userRequestDto.setAddress(address);

        try {
            userService.registerUser(userRequestDto);
            return new ModelAndView("redirect:/login");
        } catch (RuntimeException e) {
            ModelAndView modelAndView = new ModelAndView("pages/auth/register");
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
    }
}
