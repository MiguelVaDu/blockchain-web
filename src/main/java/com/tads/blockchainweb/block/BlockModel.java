package com.tads.blockchainweb.block;

import java.util.Date;

public class BlockModel {
    private int id;
    private int nonce;
    private long timeStamp;
    private String hash;
    private String previousHash;
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
    public String getHash() { return hash; }
    public int getNonce() { return nonce; }
    public long getTimeStamp() { return timeStamp; }
    public String getPreviousHash() { return previousHash; }
    public String getTransactionData() { return transactionData; }
    @Override
    public String toString() {
        return id + "-" + transactionData + "-" + hash + "-" + previousHash;
    }
}
