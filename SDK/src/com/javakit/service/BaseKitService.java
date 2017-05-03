package com.javakit.service;

import com.javakit.dao.BaseKitDAO;

import java.util.List;
import java.util.Map;

/**
 * Created by cdts on 22/03/2017.
 */
public abstract class BaseKitService<T> {

    public abstract BaseKitDAO<T> getDAO();

    public T create(T entity) {
        return getDAO().create(entity);
    }

    public T update(T entity) {
        return getDAO().update(entity);
    }

    public T loadById(String entityId) {
        return getDAO().loadById(entityId);
    }
    public List<T> loadAll() {
        return getDAO().loadAll();
    }
    public List<T> load(int page, int number) {
        return getDAO().queryPagedList(page, number);
    }
    public List<T> load(int page, int number, Map<String, String> query, String orderKey, boolean orderAsc) {
        return getDAO().queryPagedList(page, number, query, orderKey, orderAsc);
    }
    public T deleteById(String entityId) {
        T entity = loadById(entityId);
        if (entity == null)
            return null;
        return getDAO().delete(entity);
    }
}
