package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.metropolitan.subtrack.service.DashboardService;

@Controller
public class BudgetController {

    private final DashboardService dashboardService;

    public BudgetController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/budgets")
    public String budgets(Model model) {
        model.addAttribute("activePage", "budgets");
        model.addAttribute("pageTitle", "Budgets");
        model.addAttribute("budget", dashboardService.getBudgetStatus());
        return "budgets";
    }
}
