package com.dzhaparov.controller.home;


import com.dzhaparov.dto.home.response.HomePageDataResponse;
import com.dzhaparov.service.home.HomeService;
import com.dzhaparov.util.AuthHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    private final HomeService homeService;
    private final AuthHelper authHelper;

    public HomeController(HomeService homeService, AuthHelper authHelper) {
        this.homeService = homeService;
        this.authHelper = authHelper;
    }

    @GetMapping
    public String homePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        HomePageDataResponse homePageData = homeService.getHomePageData(email);

        boolean isTeacher = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        String userRole = isTeacher ? "TEACHER" : "STUDENT";

        model.addAttribute("homePageData", homePageData);
        model.addAttribute("isTeacher", isTeacher);
        model.addAttribute("userRole", userRole);

        return "home";
    }
    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("userRole", authHelper.getCurrentUser().getRole().name());
        return "profile";
    }
}
