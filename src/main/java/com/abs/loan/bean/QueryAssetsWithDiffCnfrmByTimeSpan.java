package com.abs.loan.bean;

import java.util.List;

public class QueryAssetsWithDiffCnfrmByTimeSpan extends QueryBaseRespBean {
    
    private List<AssetWithDiffCnfrm> assets;// 资产列表 M 符合条件的资产列表

    public static class AssetWithDiffCnfrm {

        private String assetUid;// 资产ID String M
        private String diffCnfrmTime;// 差额划拨确认交易入链时间 String ans20 M 结束时间（不包含），yyyyMMddHHmmss

        public String getAssetUid() {
            return assetUid;
        }

        public void setAssetUid(String assetUid) {
            this.assetUid = assetUid;
        }

        public String getDiffCnfrmTime() {
            return diffCnfrmTime;
        }

        public void setDiffCnfrmTime(String diffCnfrmTime) {
            this.diffCnfrmTime = diffCnfrmTime;
        }

    }

    public List<AssetWithDiffCnfrm> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetWithDiffCnfrm> assets) {
        this.assets = assets;
    }

}

