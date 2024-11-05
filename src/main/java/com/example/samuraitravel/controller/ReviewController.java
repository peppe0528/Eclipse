package com.example.samuraitravel.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.service.ReviewService;
import com.example.samuraitravel.service.UserService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getAllReviews(Model model, Principal principal) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new ReviewForm()); // フォームオブジェクトを追加

        if (principal != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            boolean hasPostedReview = reviews.stream().anyMatch(review -> review.getUserId().equals(user.getId()));
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("hasPostedReview", hasPostedReview);
        } else {
            model.addAttribute("isLoggedIn", false);
            model.addAttribute("hasPostedReview", false);
        }

        return "reviews/list";
    }
    
    @PostMapping
    public String addReview(@ModelAttribute @Valid ReviewForm reviewForm, BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()) {
            List<Review> reviews = reviewService.getAllReviews();
            model.addAttribute("reviews", reviews);
            return "reviews/list"; // バリデーションエラーの場合、再度リストページを表示
        }
        
        if (principal != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            Review review = new Review(); // Reviewオブジェクトを初期化
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContent());
            review.setUserId(user.getId());
            reviewService.saveReview(review);
        }
        
        return "redirect:/reviews";
    }
    
    @GetMapping("/{id}")
    public String getReviewDetails(@PathVariable Long id, Model model) {
    	List<Review> review = reviewService.findByHouseId(id);
    	model.addAttribute("review", review);
    	return "reviews/detail";
    }
}
