package cn.enjoy.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 享学课堂[Roy老师]
 */
public class RoyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("royServlet接收到了请求->GET");
        resp.getWriter().write("hello roy GET");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("royServlet接收到了请求->POST");
        resp.getWriter().write("hello roy POST");
    }
}
