package com.brainfish.service;


public interface DBService
{
    public void set(String key, String value);
    public String get(String key);
    public Integer count(String value);
    public void delete(String key);
    void beginTransaction();

    public void rollbackTransaction();
    public void commitTransaction();
}
