package com.javakit.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by cdts on 22/03/2017.
 */
public abstract class BaseKitDAO<T> {

    protected String getIdKey() {
        return "id";
    }

    @Autowired
    protected SessionFactory sessionFactory;

    public T create(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(entity);
            return entity;
        } catch (Exception e) {
            return null;
        }
    }

    public T loadById(String entityId) {
        return query(getIdKey(), entityId);
    }


    protected T query(String key, String value) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        CriteriaQuery<T> cq = builder.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        Predicate condition = builder.equal(root.get(key), value);
        cq.where(condition);
        List<T> list = session.createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
