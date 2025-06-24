package com.tads.blockchainweb.block;

import lombok.Getter;

import java.util.Date;

public class BlockModel {
    private int id;
    @Getter
    private int nonce;
    @Getter
    private long timeStamp;
    @Getter
    private String hash;
    @Getter
    private String previousHash;
    @Getter
    private String transactionData; // cadena que reúne las 3 transacciones

    public BlockModel(int id, String transactionData, String previousHash) {
        this.id = id;
        this.transactionData = transactionData;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.nonce = 0;
        generateHash();
    }
    public void generateHash() {
        String dataToHash = Integer.toString(id) + previousHash
                + Long.toString(timeStamp) + Integer.toString(nonce)
                + transactionData;
        this.hash = SHA256Helper.generateHash(dataToHash);
    }
    public void incrementNonce() {
        this.nonce++;
    }

    @Override
    public String toString() {
        return id + "-" + transactionData + "-" + hash + "-" + previousHash;
    }

    public int getId() { return this.id; }
}
