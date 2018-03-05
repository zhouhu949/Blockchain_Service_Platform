package com.test;

import org.junit.Test;

import com.abs.config.CategoryHelper;
import com.alibaba.fastjson.JSONArray;

public class UtilTest {
    @Test
    public void categoryUtilTest(){
        CategoryHelper categoryUtil = CategoryHelper.getInstance();
        

        String txType = "MORTGAGE_DOC_UPLOAD";

        String cc = categoryUtil.getCategory(txType);
        String pc = categoryUtil.getPreviousCategoryExceptSelf(txType);
        JSONArray pcs = categoryUtil.getPreviousCategories(txType);
        System.out.println(cc);
        System.out.println(pc);
        System.out.println(pcs.toString());
    }
}

