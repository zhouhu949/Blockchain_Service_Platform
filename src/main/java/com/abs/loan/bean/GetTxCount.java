package com.abs.loan.bean;

import java.util.List;

public class GetTxCount extends QueryBaseRespBean {
    private List<TxCount> txCounts;// 交易数量统计 List<TxCount> M 交易数量统计信息列表

    public static class TxCount {
        private String timeStart;// 起始时间（包含） String ans20 M 起始时间（包含），yyyyMMddHHmmss
        private String timeEnd;// 结束时间（不包含） String ans20 M 结束时间（不包含），yyyyMMddHHmmss
        private Integer txNum;// 交易数 Integer M 此时间范围内所有交易数量

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

        public Integer getTxNum() {
            return txNum;
        }

        public void setTxNum(Integer txNum) {
            this.txNum = txNum;
        }

    }

    public List<TxCount> getTxCounts() {
        return txCounts;
    }

    public void setTxCounts(List<TxCount> txCounts) {
        this.txCounts = txCounts;
    }
}

