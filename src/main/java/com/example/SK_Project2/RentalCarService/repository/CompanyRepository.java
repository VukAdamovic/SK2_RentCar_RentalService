package com.example.SK_Project2.RentalCarService.repository;

import com.example.SK_Project2.RentalCarService.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository <Company,Long> {

    Optional<Company> findCompanyByName(String name);

    Optional<Company> findCompanyById(Long id);

}
