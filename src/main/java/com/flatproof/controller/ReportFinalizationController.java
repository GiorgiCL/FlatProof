package com.flatproof.controller;

import com.flatproof.dto.hash.HashVerificationResponse;
import com.flatproof.service.ReportFinalizationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportFinalizationController {

    private final ReportFinalizationService reportFinalizationService;

    public ReportFinalizationController(ReportFinalizationService reportFinalizationService) {
        this.reportFinalizationService = reportFinalizationService;
    }

    @PostMapping("/{reportId}/finalize")
    public Map<String, String> finalizeReport(@PathVariable Long reportId,
                                              Authentication authentication) {
        String userEmail = authentication.getName();
        String finalHash = reportFinalizationService.finalizeReport(reportId, userEmail);

        return Map.of(
                "message", "Report finalized successfully",
                "finalHash", finalHash
        );
    }

    @GetMapping("/{reportId}/verify-hash")
    public HashVerificationResponse verifyReportHash(@PathVariable Long reportId,
                                                     Authentication authentication) {
        String userEmail = authentication.getName();
        return reportFinalizationService.verifyReportHash(reportId, userEmail);
    }
}