package com.abs.loan.bean;

import java.math.BigDecimal;
import java.util.List;

public class GetAvgTxPerBlock extends QueryBaseRespBean {
    private List<AvgTxPerBlock> avgTxPerBlocks;// 交易数量统计 List<avgTxPerBlock> M 平均区块交易数量统计信息列表

    public static class AvgTxPerBlock {
        private String timeStart;// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        private String timeEnd;// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        private BigDecimal txPerBlock;// 区块中交易数量的平均值 BigDecimal N12,2 M 此时间范围内所有区块中交易数量的平均值

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
        }

        public BigDecimal getTxPerBlock() {
            return txPerBlock;
        }

        public void setTxPerBlock(BigDecimal txPerBlock) {
            this.txPerBlock = txPerBlock;
        }

    }

    public List<AvgTxPerBlock> getAvgTxPerBlocks() {
        return avgTxPerBlocks;
    }

    public void setAvgTxPerBlocks(List<AvgTxPerBlock> avgTxPerBlocks) {
        this.avgTxPerBlocks = avgTxPerBlocks;
    }
}

