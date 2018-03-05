package com.abs.loan.bean;

/**
 * 抵押
 */
public class Mortgage {
    protected String mortgageType;// 合同号 String n2 M 01 房产 02 车辆

    public String getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(String mortgageType) {
        this.mortgageType = mortgageType;
    }
}

