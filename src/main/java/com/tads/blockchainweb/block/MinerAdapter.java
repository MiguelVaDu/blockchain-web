package com.tads.blockchainweb.block;

import com.tads.blockchainweb.block.BlockModel;
import com.tads.blockchainweb.block.Contants;

public class MinerAdapter {
    private double reward = 0.0;

    public void mine(BlockModel block) {
        while (notGoldenHash(block)) {
            block.generateHash();
            block.incrementNonce();
        }
        // al encontrar:
        reward += Contants.MINER_REWARD;
    }
    private boolean notGoldenHash(BlockModel block) {
        String leadingZeros = new String(new char[Contants.DIFFICULTY]).replace('\0', '0');
        return !block.getHash().substring(0, Contants.DIFFICULTY).equals(leadingZeros);
    }
    public double getReward() { return reward; }
}
