package com.flatproof.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.Map;

@RestController
public class BlockchainDebugController {

    private final Web3j web3j;
    private final Credentials credentials;

    public BlockchainDebugController(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    @GetMapping("/api/test/blockchain")
    public Map<String, Object> testBlockchain() throws Exception {
        String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
        long chainId = web3j.ethChainId().send().getChainId().longValue();

        return Map.of(
                "clientVersion", clientVersion,
                "chainId", chainId,
                "address", credentials.getAddress()
        );
    }
}