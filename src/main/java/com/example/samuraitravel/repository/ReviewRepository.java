package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByTitle(String title);
    List<Review> findByHouseId(Long houseId);
}
