package com.example.SK_Project2.RentalCarService.repository;

import com.example.SK_Project2.RentalCarService.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<Model,Long> {

    Optional<Model> findModelByName(String name);

    Optional<Model> findModelById(Long id);

}
