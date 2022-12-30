package com.example.SK_Project2.RentalCarService.repository;

import com.example.SK_Project2.RentalCarService.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
}
