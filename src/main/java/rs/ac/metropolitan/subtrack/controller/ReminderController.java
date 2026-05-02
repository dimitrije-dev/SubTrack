package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.metropolitan.subtrack.service.DashboardService;

@Controller
public class ReminderController {

    private final DashboardService dashboardService;

    public ReminderController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/reminders")
    public String reminders(Model model) {
        model.addAttribute("activePage", "reminders");
        model.addAttribute("pageTitle", "Reminders");
        model.addAttribute("upcomingRenewals", dashboardService.getUpcomingRenewals());
        return "reminders";
    }
}
