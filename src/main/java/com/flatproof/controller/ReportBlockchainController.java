package com.flatproof.controller;

import com.flatproof.dto.blockchain.BlockchainAnchorResponse;
import com.flatproof.dto.blockchain.BlockchainVerificationResponse;
import com.flatproof.service.ReportBlockchainService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportBlockchainController {

    private final ReportBlockchainService reportBlockchainService;

    public ReportBlockchainController(ReportBlockchainService reportBlockchainService) {
        this.reportBlockchainService = reportBlockchainService;
    }

    @PostMapping("/{reportId}/anchor")
    public BlockchainAnchorResponse anchorReport(@PathVariable Long reportId,
                                                 Authentication authentication) {
        System.out.println("ANCHOR ENDPOINT HIT");
        String userEmail = authentication.getName();
        return reportBlockchainService.anchorReport(reportId, userEmail);
    }

    @GetMapping("/{reportId}/verify-blockchain")
    public BlockchainVerificationResponse verifyAgainstBlockchain(@PathVariable Long reportId,
                                                                  Authentication authentication) {
        String userEmail = authentication.getName();
        return reportBlockchainService.verifyAgainstBlockchain(reportId, userEmail);
    }
}