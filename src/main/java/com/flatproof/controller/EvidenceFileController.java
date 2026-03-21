package com.flatproof.controller;

import com.flatproof.dto.evidence.EvidenceFileResponse;
import com.flatproof.service.EvidenceFileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvidenceFileController {

    private final EvidenceFileService evidenceFileService;

    public EvidenceFileController(EvidenceFileService evidenceFileService) {
        this.evidenceFileService = evidenceFileService;
    }

    @PostMapping("/reports/{reportId}/evidence")
    public EvidenceFileResponse uploadReportEvidence(@PathVariable Long reportId,
                                                     @RequestParam("file") MultipartFile file,
                                                     Authentication authentication) {
        String userEmail = authentication.getName();
        return evidenceFileService.uploadReportEvidence(reportId, file, userEmail);
    }

    @PostMapping("/items/{itemId}/evidence")
    public EvidenceFileResponse uploadConditionItemEvidence(@PathVariable Long itemId,
                                                            @RequestParam("file") MultipartFile file,
                                                            Authentication authentication) {
        String userEmail = authentication.getName();
        return evidenceFileService.uploadConditionItemEvidence(itemId, file, userEmail);
    }

    @GetMapping("/reports/{reportId}/evidence")
    public List<EvidenceFileResponse> getReportEvidence(@PathVariable Long reportId,
                                                        Authentication authentication) {
        String userEmail = authentication.getName();
        return evidenceFileService.getReportEvidence(reportId, userEmail);
    }
}