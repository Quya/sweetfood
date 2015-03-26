package com.quya.common.utils.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * @author wangyanjun
 * @author lihongxi
 * @Date 2007-4-5
 */
public final class ObjectJSONBean {
    //private static HollyinfoLog log = HollyinfoLogger.getLog(ObjectJSONBean.class);
    private static ObjectJSONBean objectJsonBean;
    private ObjectJSONBean() {
    }
    public static ObjectJSONBean instance() {
        if (objectJsonBean == null) {
            objectJsonBean = new ObjectJSONBean();
            return objectJsonBean;
        }
        return objectJsonBean;
    }
    /**
     * @param bean
     * @return
     * @throws Exception
     */
    public JSONObject javaBeanToJSONObject(Object bean) throws Exception {
        org.json.simple.JSONObject json = new org.json.simple.JSONObject();
        PropertyDescriptor[] descriptors = getBeanInfo(bean).getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            String fieldName = descriptors[i].getName();
            if (fieldName.equals("class")) { continue; }
            Object fieldValue = descriptors[i].getReadMethod().invoke(bean, new Object[] {});
            if (fieldValue == null) {
                json.put(fieldName, "");
                continue;
            }
            fieldValue = handleFieldValue(fieldValue);
            if (fieldValue.getClass().isAssignableFrom(java.lang.Boolean.class)) {
                json.put(fieldName, fieldValue.toString());
            } else {
              json.put(fieldName, fieldValue);
            }
        }
        return json;
    }
    /**
     * 处理javabean属性的值，将它转化为JSONObject支持的类型
     * @param fieldValue
     * @return
     * @throws Exception
     */
    private Object handleFieldValue(final Object fieldValue) throws Exception {
        Object returnValue = fieldValue;
        Class clazz = fieldValue.getClass();
        if (clazz.isArray()) {
            returnValue = javaArrayToJSONArray(fieldValue);
        } else if (java.util.Date.class.isAssignableFrom(clazz)) {
            returnValue = com.quya.common.utils.DateUtils.format((java.util.Date) fieldValue);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            returnValue = javaCollectionToJSONArray((Collection) fieldValue);
        } else if (isBean(clazz)) {
            returnValue = javaBeanToJSONObject(fieldValue);
        }
        return returnValue;
    }
    /**
     * 将java数组转换成JSONArray
     * @param array
     * @return
     * @throws Exception
     */
    public JSONArray javaArrayToJSONArray(Object array) throws Exception {
        JSONArray jsonArray = new JSONArray();
        Class clazz = array.getClass().getComponentType();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object object = Array.get(array, i);
            if (isBean(clazz)) {
                jsonArray.add(javaBeanToJSONObject(object));
            } else if (Collection.class.isAssignableFrom(clazz)) {
                /*clazz的超类是一个集合*/
                Collection collection = (Collection) object;
                jsonArray.add(javaCollectionToJSONArray(collection));
            } else {
                jsonArray.add(object);
            }
        }
        return jsonArray;
    }
    /**
     * 将java集合转换成JSONArray
     * @param collection
     * @return
     * @throws Exception
     */
    public JSONArray javaCollectionToJSONArray(Collection collection) throws Exception {
        JSONArray jsonArray = new JSONArray();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item == null)
                continue;
                //System.out.println("ssss---------------");
            Class clazz = item.getClass();
            if (isBean(clazz)) {
                item = javaBeanToJSONObject(item);
            } else if (Collection.class.isAssignableFrom(clazz)) {
                /*item的超类是一个集合*/
                item = javaCollectionToJSONArray((Collection) item);
            } else if (clazz.isArray()) {
                /*items是一个数组*/
                item = javaArrayToJSONArray(item);
            }
            jsonArray.add(item);
        }
        return jsonArray;
    }
    /**
     * @param bean
     * @return
     * @throws IntrospectionException
     */
    private BeanInfo getBeanInfo(Object bean)
            throws IntrospectionException {
        java.beans.BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        return beanInfo;
    }

    /**
     * 判断一个类是否是一个javaBean
     * @param clazz
     * @return
     */
    private boolean isBean(Class clazz) {
        boolean returnValue = true;
        if (clazz.isPrimitive()) {
            returnValue = false;
        } else if (clazz.equals(Short.class) || clazz.equals(Integer.class) || clazz.equals(Long.class)) {
            returnValue = false;
        } else if (clazz.equals(Byte.class) || clazz.equals(Boolean.class) || clazz.equals(String.class)) {
            returnValue = false;
        } else if (clazz.equals(Float.class) || clazz.equals(Double.class) || clazz.equals(Character.class)) {
            returnValue = false;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            /*clazz的超类是一个集合*/
            returnValue = false;
        } else if (clazz.isArray()) {
            returnValue = false;
        } else if (java.util.Date.class.isAssignableFrom(clazz)) {
            returnValue = false;
        }
        return returnValue;
    }
//    private boolean isBean(Class clazz) {
//        java.lang.reflect.Field [] fields = clazz.getDeclaredFields();
//        boolean isbean = true;
//        if (fields.length == 0) {
//            isbean = false;
//        }
//        for (int i = 0; i < fields.length; i++) {
//            try {
//                java.lang.reflect.Constructor constructor = clazz.getConstructor(new Class[] {});
//                if (constructor == null) {
//                    isbean = false;
//                    break;
//                } else if (!Modifier.isPublic(constructor.getModifiers())) {
//                    isbean = false;
//                    break;
//                } else if (!isJavaBean(clazz, fields, i)) {
//                    isbean = false;
//                    break;
//                }
//
//            } catch (java.lang.NoSuchMethodException e) {
//                return false;
//            }
//        } return isbean;
//    }

    /**
     * @param clazz
     * @param fields
     * @param isbean
     * @param i
     * @return
     */
//    private boolean isJavaBean(Class clazz, Field[] fields, int i) {
//        java.lang.reflect.Method [] methods = clazz.getDeclaredMethods();
//        String fieldname = null;
//        if (fields[i].getName().charAt(0) == '_') {
//            fieldname = fields[i].getName().substring(1, fields[i].getName().length());
//        }
//        int count = 0;
//        for (int k = 0; k < methods.length; k++) {
//            String methodname = methods[k].getName();
//            if (fieldname != null && methodname.toLowerCase().
//                    indexOf(fieldname.toLowerCase()) != -1) {
//                if (methodname.startsWith("get") || methodname.startsWith("set")
//                        || methodname.startsWith("is")) {
//                    count++;
//                }
//            }
//        }
//        if (count < 2) {
//            return false;
//        }
//        return true;
//   }
    /**
     * 将JSONObject转换成JSONArray
     * @param jsonObject
     * @param params
     * @return
     * @throws Exception
     */
    public JSONArray jsonObjectToJsonArray(JSONObject jsonObject, String[] names)throws Exception {
        JSONArray jsonArray = new JSONArray();
        if (names.length > 0) {
            for (int i = 0; i < names.length; i++) {
                if (jsonObject.get(names[i]) != null && !jsonObject.get(names[i]).equals("")) {
                    jsonArray.add(jsonObject.get(names[i]));
                } else {
                    jsonArray.add("");
                }
            }
        }
        return jsonArray;
    }

    public JSONArray jsonObjectToJsonArray(JSONObject jsonObject, JSONArray jsonArrayNames)throws Exception {
        JSONArray jsonArray = new JSONArray();
        if (jsonArrayNames.size() > 0) {
            for (int i = 0; i < jsonArrayNames.size(); i++) {
                if (jsonObject.get(jsonArrayNames.get(i)) != null &&
                        !jsonObject.get(jsonArrayNames.get(i).toString()).equals("")) {
                    jsonArray.add(jsonObject.get(jsonArrayNames.get(i)));
                } else {
                    jsonArray.add("");
                }
            }
        }
        return jsonArray;
    }

    /**
     * 将JSONArray转换成JSONObject
     * @param jsonObject
     * @param params
     * @return
     * @throws Exception
     */
    public JSONObject jsonArrayToJsonObject(JSONArray jsonArray, String[] names)throws Exception {
        JSONObject newObject = new JSONObject();
        if (jsonArray.size() > 0 && names.length > 0) {
            if (jsonArray.size() == names.length) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    newObject.put(names[i], jsonArray.get(i));
                }
            }
        }
        return newObject;
    }

    /**
     * 将JSONArray转换成JSONArray二维数组
     * @param jsonObject
     * @param params
     * @return
     * @throws Exception
     */
    public JSONArray jsonObjectArrayToJsonArray(JSONArray jsonArray, String[] names)throws Exception {
        JSONArray newArray = new JSONArray();
        if (jsonArray.size() > 0) {
            for (int y = 0; y < jsonArray.size(); y++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(y);
                newArray.add(this.jsonObjectToJsonArray(jsonObject, names));
            }
        }
        return newArray;
    }

    public JSONArray jsonObjectArrayToJsonArray(JSONArray jsonArray, JSONArray jsonArrayNames)throws Exception {
        JSONArray newArray = new JSONArray();
        if (jsonArray.size() > 0) {
            for (int y = 0; y < jsonArray.size(); y++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(y);
                newArray.add(this.jsonObjectToJsonArray(jsonObject, jsonArrayNames));
            }
        }
        return newArray;
    }

    /**
     * 将String[]转换成JSONArray数组
     * @param args
     * @return
     * @throws Exception
     */
    public JSONArray stringArrayToJsonArray(String[] args) throws Exception {
        JSONArray newArray = new JSONArray();
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                newArray.add(args[i]);
            }
        }
        return newArray;
    }
    /**
     * 根据属性名数组，将属性值集合数组转换成JSONObject数组
     * @param names
     * @param values
     * @return
     * @throws Exception
     */
    public JSONArray jsonArrayToJsonObjectArray(JSONArray names , JSONArray values) throws Exception {
        JSONArray result = new JSONArray();
        final int namesLength = names.size();
        Iterator iterator = values.iterator();
        while (iterator.hasNext()) {
            JSONArray value = (JSONArray) iterator.next();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < namesLength; i++) {
                obj.put(names.get(i), value.get(i));
            }
            result.add(obj);
        }
        return result;
    }
}
