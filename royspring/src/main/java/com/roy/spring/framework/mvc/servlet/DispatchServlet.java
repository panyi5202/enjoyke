package com.roy.spring.framework.mvc.servlet;

import com.roy.spring.demo.MyAction;
import com.roy.spring.framework.context.RoyApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Roy
 */
public class DispatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("1111");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("开始启动...");
        RoyApplicationContext applicationContext = new RoyApplicationContext(
                servletConfig.getInitParameter("configLocation"));
        System.out.println("启动成功");
        MyAction myAction = (MyAction)applicationContext.getBean("myAction");
        myAction.info("2345678");
    }
}
