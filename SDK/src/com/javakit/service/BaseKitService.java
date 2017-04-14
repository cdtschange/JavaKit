package com.javakit.service;

import com.javakit.dao.BaseKitDAO;

import java.util.List;

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
        return getDAO().loadAll();
    }
    public T deleteById(String entityId) {
        T entity = loadById(entityId);
        if (entity == null)
            return null;
        return getDAO().delete(entity);
    }
}
