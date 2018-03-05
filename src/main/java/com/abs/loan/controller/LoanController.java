package com.abs.loan.controller;

public interface LoanController {

    /**
     * 3.1.1 贷款申请
     * 
     * @param json
     */
    String loanApply(String json);

    /**
     * 3.1.2 贷款审批
     * 
     * @param json
     */
    String loanApprove(String json);

    /**
     * 3.1.3 放款结果通知
     * 
     * @param json
     */
    String loanResultNotify(String json);

    /**
     * 3.1.4 放款结果确认
     * 
     * @param json
     */
    String loanResultConfirm(String json);

    /**
     * 3.2.1 质押文件上传
     *
     * @param json
     */
    String mortgageDocUpload(String json);

    /**
     * 3.2.2 质押文件确认
     *
     * @param json
     */
    String mortgageDocConfirm(String json);


    /**
     * 3.3.1差额划拨结果通知 (/loan/diffResultNotify)
     *
     * @param json
     */
    String diffResultNotify(String json);

    /**
     * 3.3.2差额划拨结果确认 (/loan/diffResultConfirm)
     *
     * @param json
     */
    String diffResultConfirm(String json);

    /**
     * 3.4.1 还款计划上传
     *
     * @param json
     */
    String repayPlanUpload(String json);

    /**
     * 3.4.2 还款计划确认
     *
     * @param json
     */
    String repayPlanConfirm(String json);

    /**
     * 3.4.3 扣款结果通知
     *
     * @param json
     */
    String repayResultNotify(String json);

    /**
     * 3.4.4 扣款结果确认
     *
     * @param json
     */
    String repayResultConfirm(String json);

    /**
     * 3.5.1 回购申请
     *
     * @param json
     */
    String buybackApply(String json);

    /**
     * 3.5.2 回购审批
     *
     * @param json
     */
    String buybackApprove(String json);

    /**
     * 3.5.3 回购结果通知
     *
     * @param json
     */
    String buybackResultNotify(String json);

    /**
     * 3.5.4 回购结果确认
     *
     * @param json
     */
    String buybackResultConfirm(String json);
}

