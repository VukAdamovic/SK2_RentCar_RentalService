package com.example.SK_Project2.RentalCarService.mapper;

import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.domain.Review;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewCreateDto;
import com.example.SK_Project2.RentalCarService.dto.review.ReviewDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    private CompanyRepository companyRepository;


    public ReviewMapper(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public ReviewDto reviewToReviewDto(Review review){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(review.getId());
        reviewDto.setCompanyId(review.getCompany().getId());
        reviewDto.setRate(review.getRate());
        reviewDto.setDesc(review.getDescription());


        return  reviewDto;
    }

    public Review reviewCrateDtoToReview(ReviewCreateDto reviewCreateDto){
        Review review = new Review();

        review.setRate(reviewCreateDto.getRate());
        review.setDescription(reviewCreateDto.getDesc());

        Company company = companyRepository.findCompanyById(reviewCreateDto.getCompanyId())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", reviewCreateDto.getCompanyId())));

        review.setCompany(company);

        return review;
    }
}
