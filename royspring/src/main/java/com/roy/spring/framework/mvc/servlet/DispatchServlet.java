package com.roy.spring.framework.mvc.servlet;

import com.roy.spring.framework.annotation.RoyController;
import com.roy.spring.framework.annotation.RoyRequestMapping;
import com.roy.spring.framework.annotation.RoyRequestParam;
import com.roy.spring.framework.context.RoyApplicationContext;
import com.roy.spring.framework.mvc.RoyHandlerAdapter;
import com.roy.spring.framework.mvc.RoyHandlerMapping;
import com.roy.spring.framework.mvc.RoyModeAndView;
import com.roy.spring.framework.mvc.RoyViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roy
 */
public class DispatchServlet extends HttpServlet {
//    private Map<String, RoyHandlerMapping> handlerMappingMap = new HashMap<>();
    private List<RoyHandlerMapping> handlerMappings = new ArrayList<>();
    private Map<RoyHandlerMapping,RoyHandlerAdapter> handlerAdapters = new HashMap<>();
    private List<RoyViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req,resp);
        /*
        try {
            RoyModeAndView mv = (RoyModeAndView) handler.getMethod().invoke(handler.getController(),null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RoyHandlerMapping handler = getHandler(req);
            RoyHandlerAdapter ha = getHandlerAdaper(handler);
            RoyModeAndView mv = ha.handle(req,resp,handler);
            processDispatchResult(resp,mv);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                resp.getWriter().write("500 Exception");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void processDispatchResult(HttpServletResponse resp, RoyModeAndView mv) {
        // 调用viewResolver的resolveView()方法
    }

    private RoyHandlerAdapter getHandlerAdaper(RoyHandlerMapping handler) {
        if (!handlerAdapters.isEmpty()){
            return this.handlerAdapters.get(handler);
        }
        return null;
    }

    // 将request中的url和method一一对应
    private RoyHandlerMapping getHandler(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath,"").replaceAll("/+","/");

        for (RoyHandlerMapping handlerMapping : handlerMappings) {
            Matcher matcher = handlerMapping.getUrl().matcher(url);
            if (matcher.matches()){
                return handlerMapping;
            }
        }
        return null;
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("开始启动...");
        // 初始化IOC容器
        RoyApplicationContext applicationContext = new RoyApplicationContext(
                servletConfig.getInitParameter("configLocation"));
        System.out.println("启动成功");

        initStrategies(applicationContext);
    }

    private void initStrategies(RoyApplicationContext context) {
        // springMVC中一共有九种策略，这里只实现三种

        initMultipartResolver(context); // 文件上传解析
        initLocaleResolver(context); // 本地化解析
        initThemeResolver(context); // 主题
        /* 实现1 */
        initHandlerMappings(context); // 建立请求URL到Handler的映射
        /* 实现2 */
        initHandlerAdapters(context); // 初始化参数动态匹配的适配器
        initHandlerExceptionResolvers(context);  // 初始化异常处理器
        initRequestToViewNameTranslator(context); // 直接解析请求名称到视图
        /* 实现3 */
        initViewResolvers(context); // 初始化视图解析器
        initFlashMapManager(context); // flash映射管理器
    }

    private void initHandlerMappings(RoyApplicationContext context) {
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Object controller = context.getBean(beanDefinitionName);
            Class<?> clazz = controller.getClass();

            if (!clazz.isAnnotationPresent(RoyController.class)) continue;

            String baseUrl=null;

            if (clazz.isAnnotationPresent(RoyRequestMapping.class)){
                baseUrl = clazz.getAnnotation(RoyRequestMapping.class).value();
            }
            // 扫描所有的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 方法上也必须要加RoyRequestMapping注解
                if (!method.isAnnotationPresent(RoyRequestMapping.class)) continue;

                String mUrl = method.getAnnotation(RoyRequestMapping.class).value();
                String regex = baseUrl+mUrl.replaceAll("/+","/");
                Pattern pattern = Pattern.compile(regex);
                handlerMappings.add(new RoyHandlerMapping(pattern,controller,method));
                System.out.println("mapping: "+pattern+"="+method);
            }
        }
    }

    private void initHandlerAdapters(RoyApplicationContext context) {
        for (RoyHandlerMapping handlerMapping : this.handlerMappings) {
            // 每个方法都有一个参数列表
            Map<String,Integer> paramMapping = new HashMap<>();

            Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                for (Annotation anno : annotations[i]) {
                    if (anno instanceof RoyRequestParam){
                        String paramName = ((RoyRequestParam) anno).value().trim();
                        paramMapping.put(paramName,i);
                    }
                }
            }
            // 处理非命名参数，主要是httpServletRequest和HttpServletResponse
            Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].equals(HttpServletRequest.class) ||
                parameterTypes[i].equals(HttpServletResponse.class)){
                    paramMapping.put(parameterTypes[i].getName(),i);
                }
            }

            this.handlerAdapters.put(handlerMapping,new RoyHandlerAdapter(handlerMapping,paramMapping));
        }
    }

    private void initViewResolvers(RoyApplicationContext context) {
        String tempateRoot = context.getConifg().getProperty("tempateRoot");
        String fullPath = this.getClass().getClassLoader().getResource(tempateRoot).getFile();

        File rootDir = new File(fullPath);
        for (File file : rootDir.listFiles()) {
            viewResolvers.add(new RoyViewResolver(file.getName(),file));
        }
    }



    /*=======================================================================================*/
    private void initHandlerExceptionResolvers(RoyApplicationContext context) {}
    private void initRequestToViewNameTranslator(RoyApplicationContext context) {}
    private void initFlashMapManager(RoyApplicationContext context) {}
    private void initThemeResolver(RoyApplicationContext context) {}
    private void initLocaleResolver(RoyApplicationContext context) {}
    private void initMultipartResolver(RoyApplicationContext context) {}
}
