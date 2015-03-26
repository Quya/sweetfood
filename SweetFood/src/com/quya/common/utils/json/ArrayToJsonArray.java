package com.quya.common.utils.json;

import java.util.ArrayList;
import java.util.Iterator;



public class ArrayToJsonArray extends ArrayList {
    private static final long serialVersionUID = 203496110660501472L;
    private Object[] a;
    private int size;
    public ArrayToJsonArray(Object[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        a = array;
        size = a.length;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        return (Object[]) a.clone();
    }

    public Object get(int index) {
        return a[index];
    }

    public Object set(int index, Object element) {
        Object oldValue = a[index];
        a[index] = element;
        return oldValue;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < a.length; i++) {
                if (o.equals(a[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    public boolean add(Object o) {
        // Increments modCount!!
        ensureCapacity(size + 1);
        a[size++] = o;
        return true;
    }
    public void ensureCapacity(int minCapacity) {
        modCount++;
        int oldCapacity = a.length;
        if (minCapacity > oldCapacity) {
            Object [] oldData = a;
            int newCapacity = (oldCapacity * Integer.parseInt("3")) / 2 + 1;
            if (newCapacity < minCapacity) {
             newCapacity = minCapacity;
            }
            a = new Object[newCapacity];
            System.arraycopy(oldData, 0, a, 0, size);
        }
        }
}
