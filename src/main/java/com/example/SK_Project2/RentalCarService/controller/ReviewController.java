package com.example.SK_Project2.RentalCarService.controller;

import com.example.SK_Project2.RentalCarService.dto.review.ReviewCreateDto;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewDto;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewFilterDto;
import com.example.SK_Project2.RentalCarService.security.CheckSecurity;
import com.example.SK_Project2.RentalCarService.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<List<ReviewDto>> findAll(@RequestHeader("authorization") String authorization){
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReviewDto> findById(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(reviewService.findById(id),HttpStatus.OK);
    }

    @PutMapping("/filterReview")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<List<ReviewDto>> filterReview(@RequestHeader("authorization") String authorization,@RequestBody ReviewFilterDto reviewFilterDto){
        return new ResponseEntity<>(reviewService.filterReview(reviewFilterDto),HttpStatus.OK);
    }
    //-------------------------------//

    @PostMapping("/registration")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReviewDto> add(@RequestHeader("authorization") String authorization,@RequestBody ReviewCreateDto reviewCreateDto){
        return  new ResponseEntity<>(reviewService.add(reviewCreateDto),HttpStatus.OK);
    }

    //------------------------------//

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<Boolean> delete(@RequestHeader("authorization") String authorization,@PathVariable("id") Long id){
        return new ResponseEntity<>(reviewService.delete(id),HttpStatus.OK);
    }

    //------------------------------//

    @PutMapping
    @CheckSecurity(roles = {"ROLE_CLIENT"})
    public ResponseEntity<ReviewDto> update(@RequestHeader("authorization") String authorization,@RequestBody ReviewDto reviewDto){
        return  new ResponseEntity<>(reviewService.update(reviewDto),HttpStatus.OK);
    }
}
