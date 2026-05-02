package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.metropolitan.subtrack.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("pageTitle", "Dashboard");

        model.addAttribute("subscriptions", dashboardService.getSubscriptionsForTable());
        model.addAttribute("upcomingRenewals", dashboardService.getUpcomingRenewals());
        model.addAttribute("categories", dashboardService.getSpendingCategories());
        model.addAttribute("activities", dashboardService.getRecentActivities());
        model.addAttribute("budget", dashboardService.getBudgetStatus());
        model.addAttribute("monthlyBars", dashboardService.getMonthlySpendBars());

        model.addAttribute("totalMonthlySpend", dashboardService.getTotalMonthlySpend());
        model.addAttribute("activeSubscriptions", dashboardService.getActiveSubscriptionsCount());
        model.addAttribute("upcomingRenewalsCount", dashboardService.getUpcomingRenewalsCount());
        model.addAttribute("annualProjection", dashboardService.getAnnualProjection());

        return "dashboard";
    }
}
