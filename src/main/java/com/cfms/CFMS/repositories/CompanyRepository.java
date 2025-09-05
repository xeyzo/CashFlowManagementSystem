package com.cfms.CFMS.repositories;

import com.cfms.CFMS.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    Optional<CompanyEntity> findByCompanyCode(String companyCode);
    
    Optional<CompanyEntity> findByName(String name);

    boolean existsByCompanyCode(String companyCode);

    boolean existsByName(String name);

    @Query("SELECT MAX(c.companyCode) FROM CompanyEntity c WHERE c.companyCode LIKE 'COMP%'")
    String findMaxCompanyCode();
}
