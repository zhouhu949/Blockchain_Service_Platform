package com.abs.loan.bean;

import java.util.List;

public class QueryAssetsWithNoMortgageDoc extends QueryBaseRespBean {
    private List<AssetWithNoMortgageDoc> assets;// 资产列表 符合条件的资产列表

    public static class AssetWithNoMortgageDoc {
        private String assetUid;// 资产ID String M
        private String loanResultConfirmTxId;// 放款结果确认交易ID Sting M 该资产的放款结果确认交易ID
        private String loanResultConfirmTime;// 放款结果确认交易写入区块链时间 String ans20 M
                                             // 放款结果确认交易写去区块链时间写入区块链时间， yyyyMMddHHmmss
        private String mortgageDocStatus;// 质押文件状态 Sting n2 M 01-未上传抵押文件 02-抵押文件未被确认
        
        public String getAssetUid() {
            return assetUid;
        }
        public void setAssetUid(String assetUid) {
            this.assetUid = assetUid;
        }
        public String getLoanResultConfirmTxId() {
            return loanResultConfirmTxId;
        }
        public void setLoanResultConfirmTxId(String loanResultConfirmTxId) {
            this.loanResultConfirmTxId = loanResultConfirmTxId;
        }
        public String getLoanResultConfirmTime() {
            return loanResultConfirmTime;
        }
        public void setLoanResultConfirmTime(String loanResultConfirmTime) {
            this.loanResultConfirmTime = loanResultConfirmTime;
        }
        public String getMortgageDocStatus() {
            return mortgageDocStatus;
        }
        public void setMortgageDocStatus(String mortgageDocStatus) {
            this.mortgageDocStatus = mortgageDocStatus;
        }
    }

    public List<AssetWithNoMortgageDoc> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetWithNoMortgageDoc> assets) {
        this.assets = assets;
    }
}

