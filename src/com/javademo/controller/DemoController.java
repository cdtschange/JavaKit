package com.javademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.javademo.schema.BaseController;
import com.javademo.service.DemoService;
import com.javakit.data.exception.NetError;
import com.javakit.data.exception.NetException;
import com.javakit.network.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cdts on 21/03/2017.
 */

@Controller
@RequestMapping(value = "demo", method = RequestMethod.GET)
public class DemoController extends BaseController {

    @Autowired
    DemoService demoService;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String printHello(ModelMap model) {
        model.addAttribute("msg", "Demo Hello");
        model.addAttribute("name", "cdts_change");
        return "hello";
    }

    @ResponseBody
    @RequestMapping("json")
    public List<Object> list() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("a", "1");
            map.put("b", "x");
            list.add(map);
        }
        return list;
    }

    @RequestMapping(value = "exceptionCustom", method = RequestMethod.GET)
    public String printExceptionCustom() {
        throw new NetException(NetError.BadRequest);
    }

    @RequestMapping(value = "exceptionNormal", method = RequestMethod.GET)
    public String printExceptionNormal() {
        Map<String, String> list = null;
        int count = list.size();
        return null;
    }


    @RequestMapping(value = "login", method = {RequestMethod.POST, RequestMethod.GET})
    public String login(ModelMap model, @RequestParam(required = false) String username, @RequestParam(required = false) String password) {
        if (username != null && password != null) {
            boolean result = demoService.login(username, password);
            model.addAttribute("result", result);
        }
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "requestHttp", method = RequestMethod.GET)
    public Object requestHttp(ModelMap model) {
        String result = HttpRequest.sendGet("https://app.jimu.com/hello/app/v2", null);
        ObjectMapper mapper = new ObjectMapper();
        Object json = null;
        try {
            json = mapper.readValue(result, Object.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
