package com.javakit.service;

import com.javakit.dao.BaseKitDAO;

/**
 * Created by cdts on 22/03/2017.
 */
public abstract class BaseKitService<T> {

    public abstract BaseKitDAO<T> getDAO();

    public T loadById(String entityId) {
        return getDAO().loadById(entityId);
    }

}
