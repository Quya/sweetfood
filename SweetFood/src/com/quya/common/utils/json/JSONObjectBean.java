/**
 * JSONObjectBean.java
 *
 * @Date 2007-3-29
 * @Copyright www.hollyinfo.com  2002-2007
 */
package com.quya.common.utils.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 完成JSONObject到JavaBean的转换
 * @Date 2014-8-17
 * @author liwei
 */
final class JSONObjectBean {
    // private static Log log = LogFactory.getLog(JsonBeanUtils.class);
    private static JSONObjectBean instance;

    private JSONObjectBean() {
    }

    public static JSONObjectBean instance() {
        if (instance == null) {
            instance = new JSONObjectBean();
        }
        return instance;
    }

    /**
     * 将JSONObject对象的对应值拷贝给bean的对应属性
     * @param jsonObject
     * @param bean
     */
    public void copyProperties(JSONObject jsonObject, Object bean)
            throws Exception {
        Iterator keys = jsonObject.keySet().iterator();
        String proName = null;
        Object proValue = null;
        while (keys.hasNext()) {
            proName = (String) keys.next();
            proValue = jsonObject.get(proName);
            if (proValue == null) {
                continue;
            }
            if (proValue instanceof JSONObject) {
                setJSONObjectProperty(bean, proName, (JSONObject) proValue);
            } else if (proValue instanceof JSONArray) {
                setJSONArrayProperty(bean, proName, (JSONArray) proValue);
            } else {
                setSimpleProperty(bean, proName, proValue);
            }
        }
    }

    /**
     * 将JSONArray中的值拷贝到对象数组中
     * @param jsonArray
     * @param beans
     * @throws Exception
     */
    public void copyProperties(JSONArray jsonArray, Object[] beans)
            throws Exception {
        Class componentClass = beans.getClass().getComponentType();
        Object jsonObject = null;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.get(i);
            if (jsonArray.get(i) instanceof JSONObject) {
                Object object = componentClass.newInstance();
                Array.set(beans, i, object);
                copyProperties((JSONObject) jsonObject, object);
                continue;
            }
            if (componentClass.equals(Integer.class)) {
                jsonObject = new Integer(((Number) jsonArray.get(i)).intValue());
            } else if (componentClass.equals(Short.class)) {
                jsonObject = new Short(((Number) jsonArray.get(i)).shortValue());
            } else if (componentClass.equals(Float.class)) {
                jsonObject = new Float(((Number) jsonArray.get(i)).floatValue());
            }
            Array.set(beans, i, jsonObject);
        }
    }

    /**
     * 将JSONArray中的值拷贝到对象集合中
     * @param jsonArray
     * @param beans
     * @throws Exception
     */
    public void copyProperties(JSONArray jsonArray, Collection beans)
            throws Exception {
        /* 因为集合类型无法知道它元素的类型，所以暂时无法处理 */
        // return;
    }

    /**
     * 将JSONArray中的值拷贝到原始类型数组中
     * @param jsonArray
     * @param beans
     * @param Exception
     */
    public void copyProperties(JSONArray jsonArray, Object beans)
            throws Exception {
        Class componentClass = beans.getClass().getComponentType();
        for (int i = 0; i < jsonArray.size(); i++) {
            if (componentClass.equals(Integer.TYPE)) {
                Array.setInt(beans, i, ((Number) jsonArray.get(i)).intValue());
            } else if (componentClass.equals(Short.TYPE)) {
                Array.setShort(beans, i, ((Number) jsonArray.get(i))
                        .shortValue());
            } else if (componentClass.equals(Long.TYPE)) {
                Array
                        .setLong(beans, i, ((Number) jsonArray.get(i))
                                .longValue());
            } else if (componentClass.equals(Double.TYPE)) {
                Array.setDouble(beans, i, ((Number) jsonArray.get(i))
                        .doubleValue());
            } else if (componentClass.equals(Float.TYPE)) {
                Array.setFloat(beans, i, ((Number) jsonArray.get(i))
                        .floatValue());
            } else if (componentClass.equals(Boolean.TYPE)) {
                Array.setBoolean(beans, i, ((Boolean) jsonArray.get(i))
                        .booleanValue());
            }
        }
    }

    /**
     * 处理JSONArray类型的属性
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setJSONArrayProperty(Object bean, String name, JSONArray value)
            throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        if (descriptor == null) {
            return;
        }
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object temp = null;
        Class propertyType = descriptor.getPropertyType();
        if (propertyType.isArray()) {
            Class componentClass = propertyType.getComponentType();
            temp = Array.newInstance(componentClass, value.size());
            writeMethod.invoke(bean, new Object[] {temp});
            if (componentClass.isPrimitive()) {
                copyProperties(value, temp);
            } else {
                copyProperties(value, (Object[]) temp);
            }
        } else if (isCollection(propertyType)) {
            copyProperties(value, (Collection) temp);
        }
    }

    /**
     * 判断一个类是否是集合类型
     * @param clazz
     * @return
     */
    private boolean isCollection(Class clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 处理JSONObject类型的属性
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setJSONObjectProperty(final Object bean, final String name,
            final JSONObject value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        if (descriptor == null) {
            return;
        }
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object property = descriptor.getPropertyType().newInstance();
        writeMethod.invoke(bean, new Object[] {property});
        copyProperties(value, property);
    }

    /**
     * 设置bean的指定属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setSimpleProperty(final Object bean, final String name,
            final Object value) throws Exception {
        Class propertyType = getPropertyType(bean, name);
        if (propertyType == null) {
            return;
        }
        if (propertyType.equals(String.class)) {
            setStringProperty(bean, name, value);
        } else if (propertyType.equals(Double.TYPE) || propertyType.equals(Double.class)) {
            setDoubleProperty(bean, name, value);
        } else if (propertyType.equals(Long.TYPE) || propertyType.equals(Long.class)) {
            setLongProperty(bean, name, value);
        } else if (propertyType.equals(Integer.TYPE) || propertyType.equals(Integer.class)) {
            setIntegerProperty(bean, name, value);
        } else if (propertyType.equals(Boolean.TYPE) || propertyType.equals(Boolean.class)) {
            setBooleanProperty(bean, name, value);
        } else if (propertyType.equals(Short.TYPE) || propertyType.equals(Short.class)) {
            setShortProperty(bean, name, value);
        } else if (propertyType.equals(Float.TYPE) || propertyType.equals(Float.class)) {
            setFloatProperty(bean, name, value);
        } else if (Date.class.isAssignableFrom(propertyType)) {
            setDateProperty(bean, name, value);
        }
    }

    /**
     * 获取属性的类型
     * @param bean
     * @param name
     * @return
     */
    private Class getPropertyType(final Object bean, final String name) {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        if (descriptor == null) {
            return null;
        }
        return descriptor.getPropertyType();
    }

    private void setDateProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            String vsr = value.toString();
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else if (vsr.length() < "yyyy-MM-dd hh:mm:ss".length()) {
                vsr += " 00:00:00";
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                tempValue = new java.sql.Date(dateFormat.parse(vsr).getTime());
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                tempValue = new java.sql.Date(dateFormat.parse(vsr).getTime());
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new java.sql.Date(((Number) value).longValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置integer类型的属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setIntegerProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Integer.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new Integer(((Number) value).intValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置Short类型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setShortProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Short.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new Short(((Number) value).shortValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置Float类型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setFloatProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Float.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new Float(((Number) value).floatValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置Long型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setLongProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Long.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new Long(((Number) value).longValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置Double类型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setDoubleProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Double.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            tempValue = new Double(((Number) value).doubleValue());
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置Boolean类型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setBooleanProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value;
        if (value instanceof String) {
            if ("".equals(((String) value).trim())
                    || "null".equals(((String) value).trim())) {
                return;
            } else {
                tempValue = Boolean.valueOf((String) value);
            }
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            if (((Number) value).intValue() == 0) {
                tempValue = Boolean.FALSE;
            } else {
                tempValue = Boolean.TRUE;
            }
        }
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 设置字符串类型属性的值
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    private void setStringProperty(final Object bean, final String name,
            final Object value) throws Exception {
        PropertyDescriptor descriptor = getPropertyDescriptor(bean, name);
        Method writeMethod = descriptor.getWriteMethod();
        if (writeMethod == null) {
            return;
        }
        Object tempValue = value.toString();
        writeMethod.invoke(bean, new Object[] {tempValue});
    }

    /**
     * 获取bean指定属性的PropertyDescriptor
     * @param bean
     * @param property
     * @return
     */
    private PropertyDescriptor getPropertyDescriptor(final Object bean,
            final String property) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(bean.getClass());
        } catch (IntrospectionException e) {
            return null;
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i].getName().equals(property)) {
                return descriptors[i];
            }
        }
        return null;
    }
}
