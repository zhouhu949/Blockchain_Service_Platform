package com.abs.loan.bean;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class QueryBlockByBlockHash extends QueryBaseRespBean {
    private BlockDetail blockContent;

    public class BlockDetail {

        private String blockHash;// 区块哈希 String M
        private Long blockHeight;// 区块高度 Long M
        private String previousBlockHash;// 上一区块哈希 String M
        private Long txNum;// 交易数 Long M
        @Field("txIDs")
        private List<String> txIds;// 交易ID列表 List<String> M 区块中所有交易ID列表
        private String blockTime;// 生成block的时间
        @Field("channelID")
        private String channelId;// 

        public String getBlockTime() {
            return blockTime;
        }

        public void setBlockTime(String blockTime) {
            this.blockTime = blockTime;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public Long getBlockHeight() {
            return blockHeight;
        }

        public void setBlockHeight(Long blockHeight) {
            this.blockHeight = blockHeight;
        }

        public String getPreviousBlockHash() {
            return previousBlockHash;
        }

        public void setPreviousBlockHash(String previousBlockHash) {
            this.previousBlockHash = previousBlockHash;
        }

        public Long getTxNum() {
            return txNum;
        }

        public void setTxNum(Long txNum) {
            this.txNum = txNum;
        }

        public List<String> getTxIds() {
            return txIds;
        }

        public void setTxIds(List<String> txs) {
            this.txIds = txs;
        }
    }

    public BlockDetail getBlockContent() {
        return blockContent;
    }

    public void setBlockContent(BlockDetail blockContent) {
        this.blockContent = blockContent;
    }


}

