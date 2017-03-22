package com.javademo.service;

import com.javademo.dao.DemoDAO;
import com.javademo.schema.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by cdts on 22/03/2017.
 */
@Service
public class DemoService extends BaseService {

    @Autowired
    DemoDAO demoDAO;

    public boolean login(String username, String password) {
        return demoDAO.login(username, password);
    }
}
