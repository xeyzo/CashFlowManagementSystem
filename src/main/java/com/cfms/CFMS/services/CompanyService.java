package com.cfms.CFMS.services;

import com.cfms.CFMS.dto.CompanyDto;
import com.cfms.CFMS.entities.CompanyEntity;
import com.cfms.CFMS.repositories.CompanyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository repo;

    public CompanyService(CompanyRepository repo) {
        this.repo = repo;
    }

    // Mapping entity -> DTO
    private CompanyDto toDTO(CompanyEntity e) {
        CompanyDto dto = new CompanyDto();
        dto.setId(e.getId());
        dto.setCompanyCode(e.getCompanyCode());
        dto.setName(e.getName());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    // Mapping DTO -> Entity (untuk create)
    private CompanyEntity toEntity(CompanyDto dto) {
        CompanyEntity e = new CompanyEntity();
        e.setCompanyCode(dto.getCompanyCode());
        e.setName(dto.getName());
        return e;
    }

    // Update entity dari DTO (supaya tidak overwrite createdAt)
    private void updateEntityFromDto(CompanyEntity e, CompanyDto dto) {
        e.setCompanyCode(dto.getCompanyCode());
        e.setName(dto.getName());
        e.setUpdatedAt(LocalDateTime.now());
    }

    public CompanyDto create(CompanyDto dto) {
        if (repo.existsByCompanyCode(dto.getCompanyCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company code already exists");
        }
        if (repo.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company name already exists");
        }

        CompanyEntity entity = toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        CompanyEntity saved = repo.save(entity);
        return toDTO(saved);
    }

    public List<CompanyDto> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompanyDto findById(Long id) {
        CompanyEntity e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return toDTO(e);
    }

    public CompanyDto update(Long id, CompanyDto dto) {
        CompanyEntity e = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));

        // Cek duplikasi saat update (kecuali dirinya sendiri)
        if (repo.existsByCompanyCode(dto.getCompanyCode()) && !e.getCompanyCode().equals(dto.getCompanyCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company code already exists");
        }
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
