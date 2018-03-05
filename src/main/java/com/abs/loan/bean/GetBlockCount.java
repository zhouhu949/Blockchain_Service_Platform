package com.abs.loan.bean;

import java.util.List;

public class GetBlockCount extends QueryBaseRespBean {
    private List<BlockCount> blockCounts;// 区块数量统计 List<blockCount> M 区块数量统计信息列表

    public static class BlockCount {
        private String timeStart;// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        private String timeEnd;// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        private Integer blockNum;// 区块数 Integer M 此时间范围内所有区块数量

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

        public Integer getBlockNum() {
            return blockNum;
        }

        public void setBlockNum(Integer blockNum) {
            this.blockNum = blockNum;
        }

    }

    public List<BlockCount> getBlockCounts() {
        return blockCounts;
    }

    public void setBlockCounts(List<BlockCount> blockCounts) {
        this.blockCounts = blockCounts;
    }
}

