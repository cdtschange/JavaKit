package com.javakit.dao;

import com.javakit.data.log.Log;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
            session.close();
            return entity;
        } catch (Exception e) {
            Log.error("创建失败",  e);
            return null;
        }
    }

    public T update(T entity) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
            session.close();
            return entity;
        } catch (Exception e) {
            Log.error("更新失败", e);
            return null;
        }
    }

    public T delete(T entity) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
            session.close();
            return entity;
        } catch (Exception e) {
            Log.error("删除失败", e);
            return null;
        }
    }

    public T loadById(String entityId) {
        return query(getIdKey(), entityId);
    }


    public List<T> loadAll() {
        return queryList(null);
    }


    protected T query(String key, String value) {
        return query(new HashMap<String, String>() {{
            put(key, value);
        }});
    }
    protected T query(Map<String, String> map) {
        List<T> list = queryList(map);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    protected List<T> queryList(Map<String, String> map) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        CriteriaQuery<T> cq = builder.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        if (map != null && map.size() > 0) {
            List<Predicate> conditions = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Predicate condition = builder.equal(root.get(entry.getKey()), entry.getValue());
                conditions.add(condition);
            }
            cq.where(conditions.toArray(new Predicate[0]));
        }
        List<T> list = session.createQuery(cq).getResultList();
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    public List<T> queryPagedList(int page, int number) {
        return queryPagedList(page, number, null, null, false);
    }
    public List<T> queryPagedList(int page, int number, Map<String, String> query, String orderKey, boolean orderAsc) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        CriteriaQuery<T> cq = builder.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        if (query != null && query.size() > 0) {
            List<Predicate> conditions = new ArrayList<>();
            for (Map.Entry<String, String> entry : query.entrySet()) {
                Predicate condition = builder.like(root.get(entry.getKey()), "%" + entry.getValue() + "%");
                conditions.add(condition);
            }
            cq.where(builder.or(conditions.toArray(new Predicate[0])));
        }
        if (!StringUtils.isEmpty(orderKey)) {
            if (orderAsc) {
                cq.orderBy(builder.asc(root.get(orderKey)));
            } else {
                cq.orderBy(builder.desc(root.get(orderKey)));
            }
        }
        List<T> list = session.createQuery(cq).setFirstResult(page * number).setMaxResults(number).getResultList();
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
