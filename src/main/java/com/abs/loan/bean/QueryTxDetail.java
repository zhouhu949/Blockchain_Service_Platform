package com.abs.loan.bean;

import com.abs.loan.bean.QueryTxDetails.TxDetail;

public class QueryTxDetail extends QueryBaseRespBean {
    private TxDetail txContent;

    public TxDetail getTxContent() {
        return txContent;
    }

    public void setTxContent(TxDetail txContent) {
        this.txContent = txContent;
    }
    
}

