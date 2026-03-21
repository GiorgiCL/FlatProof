package com.flatproof.service;

import com.flatproof.dto.hash.HashVerificationResponse;
import com.flatproof.entity.*;
import com.flatproof.repository.InspectionReportRepository;
import com.flatproof.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportFinalizationServiceTest {

    private InspectionReportRepository inspectionReportRepository;
    private UserRepository userRepository;
    private ReportHashService reportHashService;
    private ReportFinalizationService reportFinalizationService;

    @BeforeEach
    void setUp() {
        inspectionReportRepository = mock(InspectionReportRepository.class);
        userRepository = mock(UserRepository.class);
        reportHashService = mock(ReportHashService.class);

        reportFinalizationService = new ReportFinalizationService(
                inspectionReportRepository,
                userRepository,
                reportHashService
        );
    }

    @Test
    void verifyReportHash_shouldReturnValidTrue_whenHashesMatch() {
        User user = new User();
        user.setName("Itty");
        user.setEmail("itty@test.com");
        user.setPassword("encoded");
        user.setRole(Role.USER);        Property property = new Property(1L, "Test Property", "Address", "Desc", user);
        InspectionReport report = new InspectionReport(
                2L,
                property,
                InspectionStatus.FINALIZED,
                LocalDateTime.now(),
                LocalDateTime.now(),
                user,
                "notes",
                "abc123",
                null,
                null
        );

        when(userRepository.findByEmail("itty@test.com")).thenReturn(Optional.of(user));
        when(inspectionReportRepository.findById(2L)).thenReturn(Optional.of(report));
        when(reportHashService.generateReportHash(report)).thenReturn("abc123");

        HashVerificationResponse response =
                reportFinalizationService.verifyReportHash(2L, "itty@test.com");

        assertTrue(response.isValid());
        assertEquals("abc123", response.getCurrentHash());
        assertEquals("abc123", response.getStoredFinalHash());
    }
}