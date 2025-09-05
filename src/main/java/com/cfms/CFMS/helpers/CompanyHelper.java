package com.cfms.CFMS.helpers;

import com.cfms.CFMS.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyHelper {
    @Autowired
    private CompanyRepository companyRepository;

    // 2. Ubah metode menjadi public
    public String generateNextCompanyCode() {
        // 1. Ubah Prefix untuk menyertakan tanda hubung (-)
        final String PREFIX = "COMP-"; // DIUBAH

        // Query di repository tidak perlu diubah selama masih LIKE 'COMP%'
        String lastCode = companyRepository.findMaxCompanyCode();
        int nextSequence = 1;

        if (lastCode != null) {
            String numericPart = lastCode.substring(PREFIX.length());
            int lastSequence = Integer.parseInt(numericPart);
            nextSequence = lastSequence + 1;
        }

        // 2. Ubah format padding menjadi 2 digit
        String nextSequencePadded = String.format("%02d", nextSequence); // DIUBAH

        return PREFIX + nextSequencePadded;
    }
}
