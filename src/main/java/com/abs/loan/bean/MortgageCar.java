package com.abs.loan.bean;

import java.math.BigDecimal;
import java.util.Date;

public class MortgageCar extends Mortgage {
    private String contractNo;// 合同号 String ans40 M
    private String oriVin;// 置换的源机架号 String ans20 C 若为置换车辆时需要填写源机架号，表明要置换的车辆
    private String vin;// 车辆识别代码/车架号 String ans20 M
    private String mvrc;// 机动车登记证书编号 String ans20 M
    private String plateNo;// 车牌号码 String ans10 M
    private String vehicleType;// 车辆类型 String n2 M 01-客车 02-轿车 03-旅居车 04-普通货车 05-厢式货车 06-仓栅式货车
                               // 07-封闭货车 08-罐式货车 09-平板货车 10-集装厢车 11-自卸货车 12-无轨电车 13-有轨电车 14-二轮摩托车
                               // 15-摩托车 16-半挂车 17-全挂车 99-其他
    private String vehicleBrand;// 车辆品牌 String ans40 M
    private String vehicleModel;// 车辆型号 String ans40 M
    private String vehicleConditions;// 车况 String ans40 M
    private String vehicleColor;// 车身颜色 String ans40 M
    private String engineNo;// 发动机型号 String ans40 M
    private String sweptVolume;// 排量 String ans5 M
    private String useCharacter;// 使用性质 String ans2 M 01-运营；02-非运营；03-货运
    private String pledModel;// 抵押渠道 String C100 C
    private String mortagageDate;// 抵押登记日期 String M yyyy-MM-dd
    private Date purchaseDate;// 购买时间 Date D M yyyy-MM-dd
    private BigDecimal purchasePrice;// 购买价格 BigDecimal n10,2 M 购买金额（单位：元）
    private BigDecimal valuation;// 车辆评估价 BigDecimal n10,2 M
    private BigDecimal che300Valuation;// 车300价 BigDecimal n10,2 C 车300价格评估
    private BigDecimal loanAmount;// 贷款金额 BigDecimal n10,2 M 车辆放款额
    private BigDecimal mortgageRates;// 抵押率 BigDecimal M 抵押率=贷款金额/车估值
    private Boolean extension;// 是否展期 Boolean M 单位万元
    private String dateOfProduction;// 车辆出厂日期 String ans10 M yyyy-MM-dd
    private String issueDate;// 发证日期 String ans10 M yyyy-MM-dd
    private Date tciEndDate;// 交强险到期时间 Date D C
    private Date vaiEndDate;// 车辆年检到期时间 Date D C
    private String type;// 产品类型 String M 01-新车；02-二手；03-旧车
    private Integer transferCount;// 过户次数 Integer n3 C 二手车时必填
    private BigDecimal downPaymentRatio;// 首付比例 BigDecimal n10,2 C 有首付款时必填，首付比例=首付金额/车估值（车价）
    private BigDecimal depositRatio;// 保证金比例 BigDecimal n10,2 C 具有保证金时需填写，保证金比例=保证金/车估值（车价）
    private Date encumbranceCertUpdateDate;// 权证上传时间 Date D O 权证上传时间，yyyy-MM-dd HH:mm:ss
    
    
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getOriVin() {
        return oriVin;
    }
    public void setOriVin(String oriVin) {
        this.oriVin = oriVin;
    }
    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public String getMvrc() {
        return mvrc;
    }
    public void setMvrc(String mvrc) {
        this.mvrc = mvrc;
    }
    public String getPlateNo() {
        return plateNo;
    }
    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getVehicleBrand() {
        return vehicleBrand;
    }
    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }
    public String getVehicleModel() {
        return vehicleModel;
    }
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
    public String getVehicleConditions() {
        return vehicleConditions;
    }
    public void setVehicleConditions(String vehicleConditions) {
        this.vehicleConditions = vehicleConditions;
    }
    public String getVehicleColor() {
        return vehicleColor;
    }
    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }
    public String getEngineNo() {
        return engineNo;
    }
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }
    public String getSweptVolume() {
        return sweptVolume;
    }
    public void setSweptVolume(String sweptVolume) {
        this.sweptVolume = sweptVolume;
    }
    public String getUseCharacter() {
        return useCharacter;
    }
    public void setUseCharacter(String useCharacter) {
        this.useCharacter = useCharacter;
    }
    public String getPledModel() {
        return pledModel;
    }
    public void setPledModel(String pledModel) {
        this.pledModel = pledModel;
    }
    public String getMortagageDate() {
        return mortagageDate;
    }
    public void setMortagageDate(String mortagageDate) {
        this.mortagageDate = mortagageDate;
    }
    public Date getPurchaseDate() {
        return purchaseDate;
    }
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public BigDecimal getValuation() {
        return valuation;
    }
    public void setValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }
    public BigDecimal getChe300Valuation() {
        return che300Valuation;
    }
    public void setChe300Valuation(BigDecimal che300Valuation) {
        this.che300Valuation = che300Valuation;
    }
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    public BigDecimal getMortgageRates() {
        return mortgageRates;
    }
    public void setMortgageRates(BigDecimal mortgageRates) {
        this.mortgageRates = mortgageRates;
    }
    public Boolean getExtension() {
        return extension;
    }
    public void setExtension(Boolean extension) {
        this.extension = extension;
    }
    public String getDateOfProduction() {
        return dateOfProduction;
    }
    public void setDateOfProduction(String dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }
    public String getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    public Date getTciEndDate() {
        return tciEndDate;
    }
    public void setTciEndDate(Date tciEndDate) {
        this.tciEndDate = tciEndDate;
    }
    public Date getVaiEndDate() {
        return vaiEndDate;
    }
    public void setVaiEndDate(Date vaiEndDate) {
        this.vaiEndDate = vaiEndDate;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getTransferCount() {
        return transferCount;
    }
    public void setTransferCount(Integer transferCount) {
        this.transferCount = transferCount;
    }
    public BigDecimal getDownPaymentRatio() {
        return downPaymentRatio;
    }
    public void setDownPaymentRatio(BigDecimal downPaymentRatio) {
        this.downPaymentRatio = downPaymentRatio;
    }
    public BigDecimal getDepositRatio() {
        return depositRatio;
    }
    public void setDepositRatio(BigDecimal depositRatio) {
        this.depositRatio = depositRatio;
    }
    public Date getEncumbranceCertUpdateDate() {
        return encumbranceCertUpdateDate;
    }
    public void setEncumbranceCertUpdateDate(Date encumbranceCertUpdateDate) {
        this.encumbranceCertUpdateDate = encumbranceCertUpdateDate;
    }
}

