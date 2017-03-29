package com.javakit.data.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cdts on 21/03/2017.
 */
public class NetExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        Map<String, Object> map = new HashMap<>();
        try {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            if(e instanceof NetException) {
                NetException exception = (NetException)e;
                map.put("code", exception.error.getCode());
                map.put("message", exception.error.getMessage());
                ModelAndView mav = new ModelAndView();
                view.setAttributesMap(map);
                mav.setView(view);
                return mav;
            } else {
                map.put("message", e.toString());
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement se: e.getStackTrace()) {
                    sb.append(se.toString() + "\n");
                }
                map.put("detail", sb.toString());
            }
        } catch (Exception e1) {
            map.put("message", e.toString());
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement se: e.getStackTrace()) {
                sb.append(se.toString() + "\n");
            }
            map.put("detail", sb.toString());
        }
        return new ModelAndView("error", map);
    }
}
