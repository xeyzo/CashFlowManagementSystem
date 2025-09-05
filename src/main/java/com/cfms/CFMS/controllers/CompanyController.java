package com.cfms.CFMS.controllers;

import com.cfms.CFMS.dto.CompanyDTO.CompanyRequestDto;
import com.cfms.CFMS.dto.CompanyDTO.CompanyResponseDto;
import com.cfms.CFMS.dto.WebResponseDto;
import com.cfms.CFMS.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<WebResponseDto<CompanyResponseDto>> create(@RequestBody CompanyRequestDto dto) {
        CompanyResponseDto data = service.create(dto);

        WebResponseDto<CompanyResponseDto> response = new WebResponseDto<>(
                HttpStatus.OK.value(),
                "Success saved company.",
                data
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WebResponseDto<List<CompanyResponseDto>>> getAll() {
        List<CompanyResponseDto> data = service.findAll();

        if (data.isEmpty()){
            WebResponseDto<List<CompanyResponseDto>> failed = new WebResponseDto<>(
                    HttpStatus.OK.value(),
                    "No companies found.",
                    data
            );
            return ResponseEntity.ofNullable(failed);
        }

        WebResponseDto<List<CompanyResponseDto>> response = new WebResponseDto<>(
                HttpStatus.OK.value(),
                "All companies retrieved successfully.",
                data
        );


        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> update(@PathVariable Long id, @RequestBody CompanyResponseDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
