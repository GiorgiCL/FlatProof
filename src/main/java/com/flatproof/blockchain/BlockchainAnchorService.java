package com.flatproof.blockchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
public class BlockchainAnchorService {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${app.blockchain.contract-address}")
    private String contractAddress;

    public BlockchainAnchorService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public String anchorReport(String reportId, String reportHash) {
        try {
            Function function = new Function(
                    "anchorReport",
                    Arrays.asList(new Utf8String(reportId), new Utf8String(reportHash)),
                    List.of()
            );

            String encodedFunction = FunctionEncoder.encode(function);

            RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials);

            BigInteger gasPrice = BigInteger.valueOf(20_000_000_000L); // 20 gwei
            BigInteger gasLimit = BigInteger.valueOf(500_000L);

            EthSendTransaction response = transactionManager.sendTransaction(
                    gasPrice,
                    gasLimit,
                    contractAddress,
                    encodedFunction,
                    BigInteger.ZERO
            );

            if (response.hasError()) {
                throw new RuntimeException("Blockchain transaction failed: " + response.getError().getMessage());
            }

            return response.getTransactionHash();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to anchor report on blockchain");
        }
    }

    public String getAnchoredHash(String reportId) {
        try {
            Function function = new Function(
                    "getAnchoredReport",
                    List.of(new Utf8String(reportId)),
                    Arrays.asList(
                            TypeReference.create(Utf8String.class),
                            TypeReference.create(Utf8String.class),
                            TypeReference.create(Uint256.class),
                            TypeReference.create(Address.class)
                    )
            );

            String encodedFunction = FunctionEncoder.encode(function);

            EthCall response = web3j.ethCall(
                    Transaction.createEthCallTransaction(
                            credentials.getAddress(),
                            contractAddress,
                            encodedFunction
                    ),
                    DefaultBlockParameterName.LATEST
            ).send();

            List<org.web3j.abi.datatypes.Type> decoded = FunctionReturnDecoder.decode(
                    response.getValue(),
                    function.getOutputParameters()
            );

            if (decoded.size() < 2) {
                throw new RuntimeException("Invalid blockchain response");
            }

            return decoded.get(1).getValue().toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read anchored hash from blockchain");
        }
    }
}