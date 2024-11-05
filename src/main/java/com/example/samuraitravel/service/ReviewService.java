package com.example.samuraitravel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public void updateReview(Long id, Review updatedReview) {
        Review review = getReviewById(id).orElse(null);
        if (review != null) {
            review.setTitle(updatedReview.getTitle());
            review.setContent(updatedReview.getContent());
            reviewRepository.save(review);
        }
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> findByHouseId(Long houseId) {
        return reviewRepository.findByHouseId(houseId);
    }
}
