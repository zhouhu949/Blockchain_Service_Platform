package com.abs.loan.bean;

import java.math.BigDecimal;

public class MortgageHourse extends Mortgage {
    private String houseRegisAddrText;// 房产注册城市 String n4…10 M 使用城市编码
    private String houseRegisRegion;// 房产注册区域 String n4…10 M 使用区域编码，参照省份城市列表
    private String buildingName;// 楼盘名称 String C50 M
    private String buildingStates;// 房产状态 String n1 M 1：已出售未办房产证 2：已出房产证
    private String buildingNo;// 房产证编号 String ans40 C 房产状态为2时必填
    private BigDecimal buildingArea;// 建筑面积 BigDecimal n20,2 M 单位平方米
    private String floorRidgepole;// 楼栋 String C20 C 如：1号楼1单元
    private Integer roomNumber;// 房号 Integer n11 C 如：201
    private Integer buildingOnFloor;// 抵押房产所在楼层 Integer n11 M
    private Integer totalFloor;// 总楼层 Integer n11 M
    private String towards;// 房屋朝向 String n7 M 10-南北通透 11-其他 2004001-东 2004002-南 2004003-西 2004004-北
                           // 2004005-东南 2004006-东北 2004007-西南 2004008-西北 2004009-南北 2004010-东西
                           // houseType 户型 String n2 M 0-一居 1-两居 2-三居 3-四局 4-五居 5-复式 6-别墅 7-庄园
    private String buildingType;// 房产类型 String n2 M 0-住宅 1-商品房 2-央产房 3-经济适用房 4-按经济适用房管理 5-优惠价房改房
                                // 6-标准价房改房 7-限价房 8-回迁房 9-军产房 10-校产房 11-其它 13-公寓 14-经转商
                                // 15-未支付综合地价款（土地出让金）经济适用房 16-已购公房
    private String homeType;// 房屋类型 String n2 M 0-普通住宅 1-独栋 2-联排 3-双拼 4-叠拼 5-塔楼 6-板房 9-其他
    private BigDecimal mortgageRate;// 抵押率（%） Decimal n20,2 (一抵金额+二抵金额)/评估值*100，保留2位小数
    private String specificfactor;// 特殊因素 String n2 M 0-无 1-复式 2-LOFT 3-跃层 4-一层赠送 5-临湖 6-楼王 7-临街
    private String mortgageAddr;// 抵押物地址 String C50 M
    private Boolean isOnlyHouseForFamily;// 是否家庭名下唯一房产 Boolean M false：非一套房产；true：唯一一套房产
    private String mortgageType;// 抵押类型 String n2 M 0-一抵 1-二抵
    private Boolean isDebtor;// 房主是否为债务人 Boolean M 房主是为借款人。False-不一致；true一致
//    private RelationUser mortgagerInfo;// 抵押人信息 RelationUser C 抵押人非借款人时必填，具体内容查看关系人信息
//    private RelationUser mortgagerMemberInfo;// 抵押人家庭成员信息 RelationUser M 具体内容查看关系人信息，关系人类型填写004.
    private String ownerType;// 所有权人类型 String n2 C 房主非债务人时必填。1-所有权人共借；2-所有权人保证
    private String firstMortgageType;// 首次抵押类型 String n2 M 0-银行类；1-非银行类
    private String oneMortgageType;// 一抵类型 String n2 M 0-银行类；1-非银行类
    private BigDecimal firstMortgageMoney;// 一抵金额 BigDecimal n12,2 M
    private Integer firstMortgageDeadline;// 一抵期限 Integer n3 M 一抵期限，月单位
    private BigDecimal secondMortgageMoney;// 二抵金额 BigDecimal n12,2 C 有二抵时必填
    private String secondMortgageDeadline;// 二抵押期限 Integer n3 C 有二抵时必填
    private BigDecimal unitPrice;// 单价 BigDecimal n12,2 C 单位 元/平方米
    private String custName;// 所有权人名称 String C50 M
    private String certifCountry;// 发证国家/地区 String a3 M
    private String idType;// 证件类型 String n2 M 证件类型代码，详细见附录 证件类型编码
    private String idNo;// 证件号码 String an32 M
    private BigDecimal evalValueFinal;// 执行评估价 BigDecimal n12,2 M 综合计算后最终使用的评估价格，单位万元
//    private List<EstimateInfo> estimateInfos;// 各机构评估信息 List<EstimateInfo> M
                                 // 多个评估机构时必须选中一个，抵押率使用该机构评估值计算。具体评估信息结构见附录评估机构信息
    private String gageFinishInfo;// 抵押完成情况 String n2 M 先是有受理单（受理中），10个工作日内补齐。 1-待办；2-已办.
    private String gageFinishInfoRemark;// 抵押完成情况备注 String C100 M 受理单、他项权证的受理、完成情况
    private String notaryFinishInfo;// 公证完成情况 String n2 M 1-待办；2-已办
    private String notaryFinishInfoRemark;// 公证完成情况备注 String C100 M
    private String beforeCompletion;// 放款前条件完成情况 String n2 C 0-未完成，1-已完成
    private String beforeCompletionRemark;// 放款前条件完成情况备注 String C100 C
//    private StorageAndDelivery storageAndDelivery;// 出入库信息 StorageAndDelivery M 出入库信息，见附录出入库信息
    public String getHouseRegisAddrText() {
        return houseRegisAddrText;
    }
    public void setHouseRegisAddrText(String houseRegisAddrText) {
        this.houseRegisAddrText = houseRegisAddrText;
    }
    public String getHouseRegisRegion() {
        return houseRegisRegion;
    }
    public void setHouseRegisRegion(String houseRegisRegion) {
        this.houseRegisRegion = houseRegisRegion;
    }
    public String getBuildingName() {
        return buildingName;
    }
    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
    public String getBuildingStates() {
        return buildingStates;
    }
    public void setBuildingStates(String buildingStates) {
        this.buildingStates = buildingStates;
    }
    public String getBuildingNo() {
        return buildingNo;
    }
    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }
    public BigDecimal getBuildingArea() {
        return buildingArea;
    }
    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }
    public String getFloorRidgepole() {
        return floorRidgepole;
    }
    public void setFloorRidgepole(String floorRidgepole) {
        this.floorRidgepole = floorRidgepole;
    }
    public Integer getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }
    public Integer getBuildingOnFloor() {
        return buildingOnFloor;
    }
    public void setBuildingOnFloor(Integer buildingOnFloor) {
        this.buildingOnFloor = buildingOnFloor;
    }
    public Integer getTotalFloor() {
        return totalFloor;
    }
    public void setTotalFloor(Integer totalFloor) {
        this.totalFloor = totalFloor;
    }
    public String getTowards() {
        return towards;
    }
    public void setTowards(String towards) {
        this.towards = towards;
    }
    public String getBuildingType() {
        return buildingType;
    }
    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
    public String getHomeType() {
        return homeType;
    }
    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }
    public BigDecimal getMortgageRate() {
        return mortgageRate;
    }
    public void setMortgageRate(BigDecimal mortgageRate) {
        this.mortgageRate = mortgageRate;
    }
    public String getSpecificfactor() {
        return specificfactor;
    }
    public void setSpecificfactor(String specificfactor) {
        this.specificfactor = specificfactor;
    }
    public String getMortgageAddr() {
        return mortgageAddr;
    }
    public void setMortgageAddr(String mortgageAddr) {
        this.mortgageAddr = mortgageAddr;
    }
    public Boolean getIsOnlyHouseForFamily() {
        return isOnlyHouseForFamily;
    }
    public void setIsOnlyHouseForFamily(Boolean isOnlyHouseForFamily) {
        this.isOnlyHouseForFamily = isOnlyHouseForFamily;
    }
    public String getMortgageType() {
        return mortgageType;
    }
    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }
    public Boolean getIsDebtor() {
        return isDebtor;
    }
    public void setIsDebtor(Boolean isDebtor) {
        this.isDebtor = isDebtor;
    }
    public String getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    public String getFirstMortgageType() {
        return firstMortgageType;
    }
    public void setFirstMortgageType(String firstMortgageType) {
        this.firstMortgageType = firstMortgageType;
    }
    public String getOneMortgageType() {
        return oneMortgageType;
    }
    public void setOneMortgageType(String oneMortgageType) {
        this.oneMortgageType = oneMortgageType;
    }
    public BigDecimal getFirstMortgageMoney() {
        return firstMortgageMoney;
    }
    public void setFirstMortgageMoney(BigDecimal firstMortgageMoney) {
        this.firstMortgageMoney = firstMortgageMoney;
    }
    public Integer getFirstMortgageDeadline() {
        return firstMortgageDeadline;
    }
    public void setFirstMortgageDeadline(Integer firstMortgageDeadline) {
        this.firstMortgageDeadline = firstMortgageDeadline;
    }
    public BigDecimal getSecondMortgageMoney() {
        return secondMortgageMoney;
    }
    public void setSecondMortgageMoney(BigDecimal secondMortgageMoney) {
        this.secondMortgageMoney = secondMortgageMoney;
    }
    public String getSecondMortgageDeadline() {
        return secondMortgageDeadline;
    }
    public void setSecondMortgageDeadline(String secondMortgageDeadline) {
        this.secondMortgageDeadline = secondMortgageDeadline;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getCertifCountry() {
        return certifCountry;
    }
    public void setCertifCountry(String certifCountry) {
        this.certifCountry = certifCountry;
    }
    public String getIDType() {
        return idType;
    }
    public void setIDType(String idType) {
        this.idType = idType;
    }
    public String getIDNo() {
        return idNo;
    }
    public void setIDNo(String idNo) {
        this.idNo = idNo;
    }
    public BigDecimal getEvalValueFinal() {
        return evalValueFinal;
    }
    public void setEvalValueFinal(BigDecimal evalValueFinal) {
        this.evalValueFinal = evalValueFinal;
    }
    public String getGageFinishInfo() {
        return gageFinishInfo;
    }
    public void setGageFinishInfo(String gageFinishInfo) {
        this.gageFinishInfo = gageFinishInfo;
    }
    public String getGageFinishInfoRemark() {
        return gageFinishInfoRemark;
    }
    public void setGageFinishInfoRemark(String gageFinishInfoRemark) {
        this.gageFinishInfoRemark = gageFinishInfoRemark;
    }
    public String getNotaryFinishInfo() {
        return notaryFinishInfo;
    }
    public void setNotaryFinishInfo(String notaryFinishInfo) {
        this.notaryFinishInfo = notaryFinishInfo;
    }
    public String getNotaryFinishInfoRemark() {
        return notaryFinishInfoRemark;
    }
    public void setNotaryFinishInfoRemark(String notaryFinishInfoRemark) {
        this.notaryFinishInfoRemark = notaryFinishInfoRemark;
    }
    public String getBeforeCompletion() {
        return beforeCompletion;
    }
    public void setBeforeCompletion(String beforeCompletion) {
        this.beforeCompletion = beforeCompletion;
    }
    public String getBeforeCompletionRemark() {
        return beforeCompletionRemark;
    }
    public void setBeforeCompletionRemark(String beforeCompletionRemark) {
        this.beforeCompletionRemark = beforeCompletionRemark;
    }
    
    
}

