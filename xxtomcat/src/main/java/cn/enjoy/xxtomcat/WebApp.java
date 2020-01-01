package cn.enjoy.xxtomcat;

import javax.servlet.http.HttpServlet;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 享学课堂[Roy老师]
 * 类说明：封装一个servlet工程
 */
public class WebApp {
    // servletname和servclet实例的映射
    private Map<String,HttpServlet> servletInstances;
    // url和servletname直接的映射
    private Map<String, String> servletMapping;

    public Map<String, HttpServlet> getServletInstances() {
        return servletInstances;
    }

    public void setServletInstances(Map<String, HttpServlet> servletInstances) {
        this.servletInstances = servletInstances;
    }

    public Map<String, String> getServletMapping() {
        return servletMapping;
    }

    public void setServletMapping(Map<String, String> servletMapping) {
        this.servletMapping = servletMapping;
    }
}
