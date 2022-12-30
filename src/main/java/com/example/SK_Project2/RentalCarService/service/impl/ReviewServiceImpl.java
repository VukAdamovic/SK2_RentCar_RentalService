package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.domain.Review;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewCreateDto;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewDto;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewFilterDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.mapper.CompanyMapper;
import com.example.SK_Project2.RentalCarService.mapper.ReviewMapper;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import com.example.SK_Project2.RentalCarService.repository.ReviewRepository;
import com.example.SK_Project2.RentalCarService.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;
    private CompanyMapper companyMapper;
    private CompanyRepository companyRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper, CompanyMapper companyMapper, CompanyRepository companyRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public ReviewDto findById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::reviewToReviewDto)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id: %d does not exists.", id)));
    }

    @Override
    public List<ReviewDto> findAll() {
        List<ReviewDto> reviewList = new ArrayList<>();

        reviewRepository.findAll()
                .forEach(review -> {
                    reviewList.add(reviewMapper.reviewToReviewDto(review));
                });
        return reviewList;
    }

    @Override
    public ReviewDto add(ReviewCreateDto reviewCreateDto) {
        Review review = reviewMapper.reviewCrateDtoToReview(reviewCreateDto);

        reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(review);
    }

    @Override
    public Boolean delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id: %d does not exists.", id)));

        reviewRepository.delete(review);

        return true;
    }

    @Override
    public ReviewDto update(ReviewDto reviewDto) {
        Review review = reviewRepository.findById(reviewDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Review with id: %d does not exists.", reviewDto.getId())));

        review.setRate(reviewDto.getRate());
        review.setDescription(reviewDto.getDesc());

        Company company = companyRepository.findCompanyById(reviewDto.getCompanyId())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", reviewDto.getCompanyId())));

        review.setCompany(company);

        reviewRepository.save(review);

        return reviewMapper.reviewToReviewDto(review);
    }

    @Override
    public List<ReviewDto> filterReview(ReviewFilterDto reviewFilterDto) {
        List<ReviewDto> filterReview = new ArrayList<>();

        Company company = companyRepository.findCompanyById(reviewFilterDto.getCompany_id())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", reviewFilterDto.getCompany_id())));

        List<Review> allReviews = reviewRepository.findAll();

        for (Review review : allReviews){
            if (review.getCompany().equals(company) && review.getCompany().getCity().equals(reviewFilterDto.getCity())){
                filterReview.add(reviewMapper.reviewToReviewDto(review));
            }
        }

        return filterReview;
    }
}
