package com.quya.controller;

//import org.dom4j.DocumentException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.quya.ConstantDataManager;
import com.quya.common.utils.json.JsonBeanUtils;
import com.quya.common.utils.log.HollyinfoLog;
import com.quya.common.utils.log.HollyinfoLogger;

/**
 * 校验json格式文件的超类
 * @date 2014-8-17 14:33:04
 * @author 李伟
 */
public abstract class BaseJsonController extends MultiActionController {
    private static final HollyinfoLog LOG = HollyinfoLogger.getLog(BaseJsonController.class);

    /**
     * 辅助处理返回信息
     * @param response
     * @param successMessage 消息字符串
     * @throws IOException
     */
    protected void sendMessage(HttpServletResponse response, final String message) {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            LOG.debug("出现错误信息");
        }
        System.out.println(message);
    }
    /**
     * 将一个对象作为结构返回给页面
     * @param response
     * @param state
     * @param object
     * @throws Exception
     */
    protected void sendMessageForObject(HttpServletResponse response, final String state, final Object object) {
        JSONObject result = new JSONObject();
        result.put("state", state);
        if (isPrimitiveType(object)) {
            result.put("value", object.toString());
        } else {
            try {
                result.put("value", JsonBeanUtils.toJSONObject(object));
            } catch (Exception e) {
                LOG.error("转换返回数据为JSON格式出错", e);
                result.put("state", "0");
                result.put("value", "转换返回数据为JSON格式出错");
            }
        }
        sendMessage(response, result.toString());
    }
   
    
    
    /**
     * 判断对象是否是原始类型
     * @param object
     * @return
     */
    private boolean isPrimitiveType(Object object) {
        Class clazz = object.getClass();
        if (String.class.equals(clazz) || Boolean.class.equals(clazz) || Number.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 将一个对象指定的属性的结构返回给页面
     * @param response
     * @param state
     * @param object
     * @param fields
     * @throws Exception
     */
    protected void sendMessageForObject(HttpServletResponse response, final String state, final Object object, final String[] fields) throws Exception {
        JSONObject result = new JSONObject();
        result.put("state", state);
        JSONObject beanJSON = JsonBeanUtils.toJSONObject(object);
        JSONObject value = new JSONObject();
        for (int i = 0; i < fields.length; i++) {
            if (beanJSON.get(fields[i]) != null) {
                value.put(fields[i], beanJSON.get(fields[i]));
            } else {
                value.put(fields[i], "");
            }
        }
        result.put("value", value);
        sendMessage(response, result.toString());
    }
    

    /**
     * 将一个顺序表指定的field返回返回给页面
     * @param response
     * @param state
     * @param object
     * @param fields
     * @throws Exception
     */
    protected void sendMessageForListObject(HttpServletResponse response, final String state, final List list, final String[] fields) throws Exception {
        JSONObject result = new JSONObject();
        result.put("state", state);
        JSONArray array = new JSONArray();
        for (int i = 0 ;i < list.size();i++) {
        	Object object = list.get(i);
        	JSONObject beanJSON = JsonBeanUtils.toJSONObject(object);
            JSONObject value = new JSONObject();
            for (int j = 0; j < fields.length; j++) {
                if (beanJSON.get(fields[j]) != null) {
                    value.put(fields[j], beanJSON.get(fields[j]));
                } else {
                    value.put(fields[j], "");
                }
               
            }
            array.add(value);
        }
        result.put("value", array);
        sendMessage(response, result.toString());
    }
    
    
  
   
    
    /**
     * 将一个集合根据集合中对象指定的field返回给页面
     * @param response
     * @param state
     * @param beans
     * @param fields
     */
    protected void sendMessageForCollection(HttpServletResponse response, final String state, final Collection beans, final String[] fields) {
        try {
            JSONObject returnJsonObject = new JSONObject();
            returnJsonObject.put("state", state);
            returnJsonObject.put("value", JsonBeanUtils.collectionToJsonArray(beans, fields));
            sendMessage(response, returnJsonObject.toString());
        } catch (Exception e) {
            LOG.error("sendMessageForCollection异常" + e.getMessage(), e);
        }
    }

    /**
     * 将一个对象集合作为结果返回给页面
     * @param response
     * @param state
     * @param beans
     * @param jsonArrayName 返回页面的字段名(field)
     * @throws Exception
     */
    protected void sendMessageForCollection(HttpServletResponse response, final String state, final Collection beans, JSONArray jsonArrayName) throws Exception {
        JSONObject returnJsonObject = new JSONObject();
        returnJsonObject.put("state", state);
        returnJsonObject.put("value", JsonBeanUtils.collectionToJsonArray(beans, jsonArrayName));
        sendMessage(response, returnJsonObject.toString());
    }

    /**
     * 根据约定的参数params获取页面传过来的参数值
     * @param request
     * @return
     */
    protected String getParams(HttpServletRequest request) {
        return request.getParameter("params");
    }

    /**
     * 公用获取前台参数并较验
     * @param request
     * @return
     * @throws Exception
     */
    protected JSONObject getJSONParams(HttpServletRequest request) {
          String params = request.getParameter("params");
          return (JSONObject) JSONValue.parse(params);
     }
    /**
     * 返回系统时间的毫秒值
     * @return
     */
    protected Long getTime() {
        return new Long(ConstantDataManager.getDataBaseServerTimeMIllis());
    }

    /**
     * 获取当天的0点0分0秒对应的时间对应的毫秒数
     * @return
     */
    protected Long getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ConstantDataManager.getDataBaseServerTimeMIllis());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Long(calendar.getTimeInMillis());
    }

    /**
     * 获取当前角色列表（网报用户为所有角色；综合用户为当前角色）
     * @param request
     * @param curUserCls
     * @return
     */
    protected List getCurRoles(HttpServletRequest request, Integer curUserCls) {
       //TODO
    	return null;
    }
}