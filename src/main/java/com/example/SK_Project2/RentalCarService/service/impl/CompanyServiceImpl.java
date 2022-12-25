package com.example.SK_Project2.RentalCarService.service.impl;

import com.example.SK_Project2.RentalCarService.domain.Company;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyCreateDto;
import com.example.SK_Project2.RentalCarService.dto.company.CompanyDto;
import com.example.SK_Project2.RentalCarService.exception.NotFoundException;
import com.example.SK_Project2.RentalCarService.mapper.CompanyMapper;
import com.example.SK_Project2.RentalCarService.repository.CompanyRepository;
import com.example.SK_Project2.RentalCarService.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public List<CompanyDto> findAll() {
        List<CompanyDto> companies = new ArrayList<>();
        companyRepository.findAll()
                .forEach(company -> {
                    companies.add(companyMapper.companyToCompanyDto(company));
                });
        return companies;
    }

    @Override
    public CompanyDto findById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::companyToCompanyDto)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", id)));
    }

    @Override
    public CompanyDto add(CompanyCreateDto companyCreateDto) {
        Company company = companyMapper.companyCreateDtoToCompany(companyCreateDto);

        companyRepository.save(company);

        return companyMapper.companyToCompanyDto(company);
    }

    @Override
    public Boolean delete(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", id)));

        companyRepository.delete(company);

        return true;
    }

    @Override
    public CompanyDto update(CompanyDto companyDto) {
        Company company = companyRepository.findById(companyDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Company with id: %d does not exists.", companyDto.getId())));

        company.setId(companyDto.getId());
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setNumOfCars(companyDto.getNumOfCars());
        company.setCity(companyDto.getCity());

        companyRepository.save(company);

        return companyMapper.companyToCompanyDto(company);
    }
}
