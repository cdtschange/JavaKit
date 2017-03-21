package com.javademo.controller;

import com.javademo.schema.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by cdts on 21/03/2017.
 */

@Controller
@RequestMapping(value = "demo", method = RequestMethod.GET)
public class DemoController extends BaseController {

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String printHello(ModelMap model) {
        model.addAttribute("msg", "Demo Hello");
        model.addAttribute("name", "cdts_change");
        return "hello";
    }
}
