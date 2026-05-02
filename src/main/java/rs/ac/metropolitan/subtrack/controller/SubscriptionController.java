package rs.ac.metropolitan.subtrack.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rs.ac.metropolitan.subtrack.model.BillingCycle;
import rs.ac.metropolitan.subtrack.model.Subscription;
import rs.ac.metropolitan.subtrack.model.SubscriptionStatus;
import rs.ac.metropolitan.subtrack.service.DashboardService;
import rs.ac.metropolitan.subtrack.service.SubscriptionService;

import java.util.List;

@Controller
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final DashboardService dashboardService;

    public SubscriptionController(SubscriptionService subscriptionService, DashboardService dashboardService) {
        this.subscriptionService = subscriptionService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/subscriptions")
    public String subscriptions(Model model) {
        model.addAttribute("activePage", "subscriptions");
        model.addAttribute("pageTitle", "Subscriptions");
        model.addAttribute("subscriptions", dashboardService.getSubscriptionsForTable());
        return "subscriptions";
    }

    @GetMapping("/subscriptions/new")
    public String newSubscription(Model model) {
        Subscription subscription = new Subscription();
        subscription.setStatus(SubscriptionStatus.ACTIVE);
        subscription.setBillingCycle(BillingCycle.MONTHLY);
        model.addAttribute("activePage", "subscriptions");
        model.addAttribute("pageTitle", "Add Subscription");
        model.addAttribute("subscription", subscription);
        model.addAttribute("statuses", List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED, SubscriptionStatus.CANCELLED));
        model.addAttribute("billingCycles", List.of(BillingCycle.MONTHLY, BillingCycle.YEARLY));
        return "subscription-form";
    }

    @PostMapping("/subscriptions")
    public String createSubscription(@ModelAttribute Subscription subscription) {
        subscriptionService.createSubscription(subscription);
        return "redirect:/subscriptions";
    }

    @GetMapping("/subscriptions/{id}")
    public String subscriptionDetails(@PathVariable Long id, Model model) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> {
                    model.addAttribute("activePage", "subscriptions");
                    model.addAttribute("pageTitle", "Subscription Details");
                    model.addAttribute("subscription", subscription);
                    return "subscription-detail";
                })
                .orElse("redirect:/subscriptions");
    }

    @GetMapping("/subscriptions/{id}/edit")
    public String editSubscription(@PathVariable Long id, Model model) {
        return subscriptionService.getSubscriptionById(id)
                .map(subscription -> {
                    model.addAttribute("activePage", "subscriptions");
                    model.addAttribute("pageTitle", "Edit Subscription");
                    model.addAttribute("subscription", subscription);
                    model.addAttribute("statuses", List.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED, SubscriptionStatus.CANCELLED));
                    model.addAttribute("billingCycles", List.of(BillingCycle.MONTHLY, BillingCycle.YEARLY));
                    return "subscription-form";
                })
                .orElse("redirect:/subscriptions");
    }

    @PostMapping("/subscriptions/{id}/update")
    public String updateSubscription(@PathVariable Long id, @ModelAttribute Subscription subscription) {
        subscriptionService.updateSubscription(id, subscription);
        return "redirect:/subscriptions";
    }

    @PostMapping("/subscriptions/{id}/delete")
    public String deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return "redirect:/subscriptions";
    }
}
