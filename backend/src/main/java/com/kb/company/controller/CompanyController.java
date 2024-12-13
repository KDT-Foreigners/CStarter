package com.kb.company.controller;

import com.kb.company.dto.company.Company;
import com.kb.company.dto.company.CompanyRequest;
import com.kb.company.dto.job.JobResponse;
import com.kb.company.service.CompanyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "CompanyController", tags = "회사 공고 정보")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/company")
public class CompanyController {
    private final CompanyService service;

    @GetMapping("")
    public ResponseEntity<List<Company>> getAllNotice() {
        List<Company> notices = service.getAllNotice();

        if (notices.size() == 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(notices);
    }

    @PostMapping("")
    public ResponseEntity<Company> createCompany(@RequestBody CompanyRequest companyRequest) {
        Company company = service.createCompany(companyRequest);

        if (company == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(company);
    }

    @GetMapping("/notice")
    public ResponseEntity<List<Company>> getRecommendedCompanyList(@RequestParam("cno") int cno) {
        List<Company> companyList = service.getRecommendedCompanyList(cno);

        if (companyList == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyList);
    }

    @GetMapping("/{cpno}/job")
    public ResponseEntity<List<JobResponse>> getJobList(@PathVariable("cpno") int cpno) {
        List<JobResponse> coverLetterOfJob = service.getCoverLetterOfJob(cpno);

        if (coverLetterOfJob == null) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(coverLetterOfJob);
    }
}
