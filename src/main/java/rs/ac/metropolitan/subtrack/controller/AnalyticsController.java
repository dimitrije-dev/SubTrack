package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.metropolitan.subtrack.service.DashboardService;

@Controller
public class AnalyticsController {

    private final DashboardService dashboardService;

    public AnalyticsController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {
        model.addAttribute("activePage", "analytics");
        model.addAttribute("pageTitle", "Analytics");
        model.addAttribute("monthlyBars", dashboardService.getMonthlySpendBars());
        model.addAttribute("categories", dashboardService.getSpendingCategories());
        return "analytics";
    }
}
