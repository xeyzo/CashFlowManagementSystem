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
        final String PREFIX = "COMP-";

        String lastCode = companyRepository.findMaxCompanyCode();
        int nextSequence = 1;

        if (lastCode != null) {
            String numericPart = lastCode.substring(PREFIX.length());
            int lastSequence = Integer.parseInt(numericPart);
            nextSequence = lastSequence + 1;
        }

        String nextSequencePadded = String.format("%02d", nextSequence); // DIUBAH

        return PREFIX + nextSequencePadded;
    }
}
