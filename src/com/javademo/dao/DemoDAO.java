package com.javademo.dao;

import com.javademo.schema.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * Created by cdts on 22/03/2017.
 */
@Repository
public class DemoDAO extends BaseDAO {
    public boolean login(String username, String password) {
        if (username.equals("demo") && password.equals("demo")) {
            return true;
        }
        return false;
    }
}
