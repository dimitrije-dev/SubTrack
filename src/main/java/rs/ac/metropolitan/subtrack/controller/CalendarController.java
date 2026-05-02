package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String calendar(Model model) {
        model.addAttribute("activePage", "calendar");
        model.addAttribute("pageTitle", "Calendar");
        return "calendar";
    }
}
