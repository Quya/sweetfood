/**
 * JsonBeanUtils.java
 *
 * @Date 2007-3-26
 * @Copyright www.hollyinfo.com  2002-2007
 */
package com.quya.common.utils.json;

import java.lang.reflect.Array;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * 鐢ㄤ簬澶勭悊JSONObject鍜宩avabean涔嬮棿浜掔浉杞崲鐨勫伐鍏风被
 *
 * @Version 0.1
 * @Date 2014-7-26
 * @author liwei
 */
public class JsonBeanUtils {
    // private static HollyinfoLog log = HollyinfoLogger.getLog(JsonBeanUtils.class);

    /**
     * 鏍规嵁json鏍煎紡鐨勫瓧绗︿覆璁剧疆缁欏畾javabean鐨勫睘鎬у�銆�
     *
     * @param bean
     *            鐩爣javabean
     * @param jsonObjectString
     *            json鏍煎紡鐨勫瓧绗︿覆
     * @throws Exception
     */
    public static void populateBean(Object bean, String jsonObjectString)
            throws Exception {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonObjectString);
        populateBean(bean, jsonObject);
    }

    /**
     * 鐢↗SONObject瀵硅薄鐨勫�缁勮JavaBean瀵硅薄
     */
    public static void populateBean(Object bean, JSONObject jsonObject)
            throws Exception {
        JSONObjectBean.instance().copyProperties(jsonObject, bean);
    }

    /**
     * 鐢↗SONArray鍊肩粍瑁呮寚瀹氱被鍨嬬殑闆嗗悎
     * @param collection
     * @param clazz
     * @param jsonArray
     * @throws Exception
     */
    public static void populateCollection(Collection collection, Class clazz, JSONArray jsonArray) throws Exception {
        int size = jsonArray.size();
        Object[] array = (Object[]) Array.newInstance(clazz, size);
        JSONObjectBean.instance().copyProperties(jsonArray, array);
        for (int i = 0; i < array.length; i++) {
            collection.add(array[i]);
        }
    }
    /**
     * 鐢╦son鏁扮粍鏍煎紡鐨勫瓧绗︿覆缁勮鎸囧畾绫诲瀷鐨勯泦鍚�
     * @param collection
     * @param clazz
     * @param jsonArrayString
     * @throws Exception
     */
    public static void populateCollection(Collection collection, Class clazz, String jsonArrayString) throws Exception {
        JSONArray jsonArray = (JSONArray) JSONValue.parse(jsonArrayString);
        populateCollection(collection, clazz, jsonArray);
    }
    /**
     * 灏咼avaBean杞崲鎴怞son瀛楃涓�
     * @param javaBean
     * @return
     * @throws Exception
     */
    public static String toJSONString(Object javaBean) throws Exception {
        return toJSONObject(javaBean).toString();
    }

    /**
     * 灏唈avabean杞崲鎴怞SONObject
     * @param javaBean
     * @return
     * @throws Exception
     */
    public static JSONObject toJSONObject(Object javaBean) throws Exception {
        return ObjectJSONBean.instance().javaBeanToJSONObject(javaBean);
    }
    /**
     * 灏咼avaBean缁勬垚鐨勯泦鍚堣浆鎹㈡垚Json瀛楃涓�
     * @param javaBeans
     * @return
     * @throws Exception
     */
    public static String toJSONString(Collection javaBeans) throws Exception {
        return toJSONArray(javaBeans).toString();
    }

    /**
     * 灏唈avabean缁勬垚鐨勬暟缁勮浆鎹㈡垚JSONArray
     * @param javaBeans
     * @return
     * @throws Exception
     */
    public static JSONArray toJSONArray(Collection javaBeans) throws Exception {
        return ObjectJSONBean.instance().javaCollectionToJSONArray(javaBeans);
    }

    /**
     * 灏咼SONObject杞崲鎴怞SONArray
     * @param jsonObject
     * @param names
     * @return
     * @throws Exception
     */
    public static JSONArray jsonObjectToJsonArray(JSONObject jsonObject, String[] names) throws Exception {
        return ObjectJSONBean.instance().jsonObjectToJsonArray(jsonObject, names);
    }

    /**
     * 灏咼SONArray杞崲鎴怞SONObject
     * @param jsonArray
     * @param names
     * @return
     * @throws Exception
     */
    public static JSONObject jsonArrayToJsonObject(JSONArray jsonArray, String[] names) throws Exception {
        return ObjectJSONBean.instance().jsonArrayToJsonObject(jsonArray, names);
    }

    /**
     * 灏咼SONArray杞崲鎴怞SONArray浜岀淮鏁扮粍
     * @param jsonObject
     * @param names
     * @return
     * @throws Exception
     */
    public static JSONArray jsonObjectArrayToJsonArray(JSONArray jsonArray, String[] names) throws Exception {
        return ObjectJSONBean.instance().jsonObjectArrayToJsonArray(jsonArray, names);
    }

    public static JSONArray jsonObjectArrayToJsonArray(JSONArray jsonArray, JSONArray jsonArrayNames) throws Exception {
        return ObjectJSONBean.instance().jsonObjectArrayToJsonArray(jsonArray, jsonArrayNames);
    }

    /**
     * 灏唋ist杞崲鎴怞SONArray浜岀淮鏁扮粍
     * @param jsonObject
     * @param names
     * @return
     * @throws Exception
     */
    public static JSONArray collectionToJsonArray(Collection collection, String[] names) throws Exception {
        JSONArray jsonArray = JsonBeanUtils.toJSONArray(collection);
        return jsonObjectArrayToJsonArray(jsonArray, names);
    }

    public static JSONArray collectionToJsonArray(Collection collection, JSONArray jsonArrayNames) throws Exception {
        JSONArray jsonArray = JsonBeanUtils.toJSONArray(collection);
        return jsonObjectArrayToJsonArray(jsonArray, jsonArrayNames);
    }

    /**
     * 灏哠tring[]杞崲鎴怞SONArray鏁扮粍
     * @param args
     * @return
     * @throws Exception
     */
    public static JSONArray strArrayToJsonArray(String[] args) throws Exception {
        return ObjectJSONBean.instance().stringArrayToJsonArray(args);
    }
    /**
     * 鏍规嵁灞炴�鍚嶆暟缁勶紝灏嗗睘鎬у�闆嗗悎鏁扮粍杞崲鎴怞SONObject鏁扮粍
     * @param names
     * @param values
     * @return
     * @throws Exception
     */
    public static JSONArray jsonArrayToJsonObjectArray(JSONArray names , JSONArray values) throws Exception {
        return ObjectJSONBean.instance().jsonArrayToJsonObjectArray(names, values);
    }
}
