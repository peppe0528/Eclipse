package com.example.samuraitravel.controller;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.form.ReviewForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.ReviewService;
import com.example.samuraitravel.service.UserService;

@Controller
@RequestMapping("/houses")
public class HouseController {

    private final HouseRepository houseRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;
    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "price", required = false) Integer price,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
        Page<House> housePage;

        if (keyword != null && !keyword.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }
        } else if (area != null && !area.isEmpty()) {
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
            } else {
                housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
            }
        } else if (price != null) {
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        } else {
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        model.addAttribute("housePage", housePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("price", price);
        model.addAttribute("order", order);

        return "houses/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, Model model, Principal principal) {
        House house = houseRepository.getReferenceById(id);
        List<Review> reviews = reviewService.findByHouseId(id);  // Long型に対応
        model.addAttribute("house", house);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewForm", new ReviewForm());
        model.addAttribute("reservationInputForm", new ReservationInputForm());

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

        return "houses/show";
    }

    @PostMapping("/{id}/reviews")
    public String addReview(@PathVariable(name = "id") Long id, @ModelAttribute @Valid ReviewForm reviewForm, BindingResult result, Principal principal, Model model) {
        if (result.hasErrors()) {
            return show(id, model, principal);  // バリデーションエラーの場合に再表示
        }

        if (principal != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.findByEmail(userDetails.getUsername());
            Review review = new Review();
            review.setHouseId(id);  // Long型に対応
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContent());
            review.setUserId(user.getId());
            reviewService.saveReview(review);
        }

        return "redirect:/houses/" + id;
    }
}