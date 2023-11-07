package com.greenblat.cloudfilestorage.controller;

import com.greenblat.cloudfilestorage.dto.AuthRequest;
import com.greenblat.cloudfilestorage.service.AuthService;
import com.greenblat.cloudfilestorage.validation.groups.OnCreate;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") AuthRequest request) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registrationPerform(@Validated({Default.class, OnCreate.class}) AuthRequest request,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", request);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }

        authService.saveUser(request);
        return "redirect:/login";
    }
}
