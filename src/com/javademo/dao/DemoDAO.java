package com.javademo.dao;

import com.javademo.entity.UserEntity;
import com.javademo.schema.BaseDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdts on 22/03/2017.
 */
@Repository
public class DemoDAO extends BaseDAO {
    @Autowired
    SessionFactory sessionFactory;

    public boolean login(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = cq.from(UserEntity.class);
        Predicate condition1 = builder.equal(root.get("username"), username);
        Predicate condition2 = builder.equal(root.get("password"), password);
        cq.where(condition1, condition2);
        List list = session.createQuery(cq).getResultList();
        return list.size() > 0;
    }
}
