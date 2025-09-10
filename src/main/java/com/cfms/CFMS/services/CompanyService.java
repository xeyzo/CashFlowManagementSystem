package com.cfms.CFMS.services;

import com.cfms.CFMS.dto.CompanyDTO.CompanyRequestDto;
import com.cfms.CFMS.dto.CompanyDTO.CompanyResponseDto;
import com.cfms.CFMS.entities.CompanyEntity;
import com.cfms.CFMS.helpers.CompanyHelper;
import com.cfms.CFMS.repositories.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository repo;

    @Autowired
    private CompanyHelper companyCodeHelper;


    public CompanyService(CompanyRepository repo) {
        this.repo = repo;
    }

    // Mapping entity -> DTO
    private CompanyResponseDto toDTO(CompanyEntity e) {
        CompanyResponseDto dto = new CompanyResponseDto();
        dto.setId(e.getId());
        dto.setCompanyCode(e.getCompanyCode());
        dto.setName(e.getName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }


    private void updateEntityFromDto(CompanyEntity e, CompanyResponseDto dto) {
        e.setName(dto.getName());
        e.setUpdatedAt(LocalDateTime.now());
    }

    public CompanyResponseDto create(CompanyRequestDto dto) {

        if (repo.existsByName(dto.getName())) {
            throw new IllegalStateException("Company name '" + dto.getName() + "' already exists.");
        }

        String nextCompanyCode = companyCodeHelper.generateNextCompanyCode();
        String companyName = dto.getName();

        CompanyEntity newEntity = new CompanyEntity();
        newEntity.setCompanyCode(nextCompanyCode);
        newEntity.setName(companyName);

        CompanyEntity savedEntity = repo.save(newEntity);

        return toDTO(savedEntity);
    }

    public List<CompanyResponseDto> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompanyResponseDto findById(Long id) {
        CompanyEntity e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return toDTO(e);
    }

    public CompanyResponseDto update(Long id, CompanyResponseDto dto) {
        CompanyEntity e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        if (repo.existsByName(dto.getName()) && !e.getName().equals(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
        }

        updateEntityFromDto(e, dto);

        CompanyEntity saved = repo.save(e);
        return toDTO(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        repo.deleteById(id);
    }
}
