package com.abs.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptUtil {
    private static ScriptEngineManager manager = new ScriptEngineManager();
    
    /**
     * @param val as: 3 
     * @param category as:CAT_ADD_REPAY:t
     * @return as: CAT_ADD_REPAY:2
     * @throws ScriptException
     */
    public static String getCategory(int val, String category) throws ScriptException{
        if(category.indexOf(":")>-1){
            String[] s = category.split(":");
            String categorySub = s[0];
            String expression = s[1];
            long value = getLongValue(val,expression);
            return categorySub+":"+value;
        }else{
            return category;
        }
    }
    public static boolean getBooleanValue(int val, String expression) throws ScriptException {
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("t", val);
        Boolean b = (Boolean) engine.eval(expression);
        return b;
    }

    public static long getLongValue(int val, String expression) throws ScriptException {
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("t", val);
        long result = 0;
        Object obj = engine.eval(expression);
        try {
            result = Math.round((Double) obj);
        } catch (ClassCastException e) {
            result = (Integer) obj;
        }
        return result;
    }

}

