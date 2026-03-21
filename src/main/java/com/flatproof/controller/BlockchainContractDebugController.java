package com.flatproof.controller;

import com.flatproof.blockchain.BlockchainAnchorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BlockchainContractDebugController {

    private final BlockchainAnchorService blockchainAnchorService;

    public BlockchainContractDebugController(BlockchainAnchorService blockchainAnchorService) {
        this.blockchainAnchorService = blockchainAnchorService;
    }

    @GetMapping("/api/test/blockchain/read")
    public Map<String, Object> testRead() {
        String hash = blockchainAnchorService.getAnchoredHash("999999");
        return Map.of("hash", hash);
    }
}