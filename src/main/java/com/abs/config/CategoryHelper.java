package com.abs.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class CategoryHelper {
    Logger logError = LoggerFactory.getLogger("operation");

    private CategoryHelper() {
        init();
    }

    public static CategoryHelper getInstance() {
        return CategoryUtilInstance.categoryUtil;
    }

    private static class CategoryUtilInstance {
        private static CategoryHelper categoryUtil = new CategoryHelper();
    }


    private Map<String, JSONObject> map = new HashMap<String, JSONObject>();
    private Set<String> txTypes;

    public Map<String, JSONObject> getMap() {
        return map;
    }

    public Set<String> getTxTypes() {
        if (txTypes == null || txTypes.size() == 0) {
            txTypes =  new HashSet<String>();
            txTypes.addAll(map.keySet());
            for (String txType : map.keySet()) {
                if (txType.indexOf(":") != -1) {
                    txTypes.add(txType.split(":")[0]);
                }
            }
        }
        return txTypes;
    }

    //
    /**
     * 获取category
     * 
     * @param txType
     * @return
     */
    public String getCategory(String txType) {
        return map.get(txType).getString("category");
    }

    /**
     * 获取除本身外的前序category,若没有则是本身
     * 
     * @param txType
     * @return
     */
    public String getPreviousCategoryExceptSelf(String txType) {
        String self = getCategory(txType);
        JSONArray jsonArr = getPreviousCategories(txType);
        if (jsonArr.size() == 1) {
            return getCategory(txType);
        }
        for (int i = 0; i < jsonArr.size(); i++) {
            String c = jsonArr.getString(i);
            if (!self.equals(c)) {
                return c;
            }
        }
        return "";
    }

    /**
     * 获取前序category
     * 
     * @param txType
     * @return
     */
    public JSONArray getPreviousCategories(String txType) {
        return map.get(txType).getJSONArray("previousCategories");
    }

    /**
     * 读取文件的category信息，放入Map<txType,category>中
     */
    private void init() {
        try {
            String classPath = CategoryHelper.class.getResource("/").getPath();
            String filePath =
                    String.format("%sinit%sprevious-category.json", classPath, File.separator);
            File file = new File(filePath);
            BufferedReader br = null;
            StringBuffer sb = new StringBuffer();
            try {
                br = new BufferedReader(new FileReader(file));
                String tem = "";
                while ((tem = br.readLine()) != null) {
                    sb.append(tem);
                }
            } catch (FileNotFoundException e) {
                logError.error("文件未找到", e);
            } catch (IOException e) {
                logError.error("", e);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            JSONArray jsons = JSON.parseArray(sb.toString());
            if (jsons != null) {
                for (int i = 0; i < jsons.size(); i++) {
                    JSONObject jsonObj = jsons.getJSONObject(i);
                    map.put(jsonObj.getString("name"), jsonObj);
                }
            }
        } catch (Exception e) {
            logError.error("CategoryHelper初始化失败", e);
        }
    }
}

