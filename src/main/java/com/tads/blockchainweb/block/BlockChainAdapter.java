package com.tads.blockchainweb.block;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockChainAdapter {
    private List<BlockModel> chain = new ArrayList<>();

    public void addBlock(BlockModel block) { chain.add(block); }
    public int size() { return chain.size(); }
    public String getLatestHash() {
        if (chain.isEmpty()) {
            return Contants.GENESIS_PREV_HASH;
        } else {
            return chain.get(chain.size()-1).getHash();
        }
    }
}
