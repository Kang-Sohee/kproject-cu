package com.ohdocha.cu.kprojectcu.util;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class DataMessage extends HashMap<String, Object> {

    public DataMessage addData(String key, Object object) {
        put(key, object);
        return this;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object obj = get(key);
        return obj != null ? (int) obj : defaultValue;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        Object obj = get(key);
        return obj != null ? (long) obj : defaultValue;
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue) {
        Object obj = get(key);
        return obj != null ? (float) obj : defaultValue;
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        Object obj = get(key);
        return obj != null ? (double) obj : defaultValue;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object obj = get(key);
        return obj != null ? (String) obj : defaultValue;
    }

    public <T> T getObject(String key, Class<T> classOfT) {
        Object obj = get(key);
        return obj != null ? (T) obj : null;
    }

    public <T> List<T> getListObject(String key, Class<T> classOfT) {
        Object obj = get(key);
        if (obj == null) return null;
        return (List<T>) obj;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
