package cn.enjoy.xxtomcat;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 享学课堂[Roy老师]
 * 类说明：webapp加载器，用来加载servlet的class文件，并创建servlet的实例对象
 */
public class WebAppLoader {
    public static WebApp loadWebapp() throws Exception {
        // 需要加载的servlet工程的根目录
        String webappPath = "D:\\workspace\\enjoyke\\out\\artifacts\\xxservlet_war_exploded\\WEB-INF";

        // servlet
        Map<String, HttpServlet> servletInstances = new HashMap<>();
        // url与serlvet name的mapping
        Map<String, String> servletMapping = new HashMap<>();

        // 创建类类加载器
        URL classBase = new URL("file:" + webappPath + "\\classes\\");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{classBase});

        // 解析xml文件
        // 1、创建解析器
        SAXReader saxReader = new SAXReader();
        // 2、获取文档对象
        Document document = saxReader.read(new File(webappPath + "\\web.xml"));
        // 3、获取根元素
        Element rootElement = document.getRootElement();
        // 4、获取根元素下的子元素
        List<Element> elementList = rootElement.elements();
        // 5、遍历子元素
        for (Element element : elementList) {
            // 6、处理servlet标签
            if ("servlet".equals(element.getName())) {
                // 获取servlet-name
                String servletName = element.element("servlet-name").getText();
                // 获取servlet-class
                String servletClass = element.element("servlet-class").getText();
                // 加载class（反射）
                Class<?> servletClazz = urlClassLoader.loadClass(servletClass);
                // 通过反射创建对象实例
                HttpServlet servlet = (HttpServlet) servletClazz.newInstance();
                servletInstances.put(servletName, servlet);
                System.out.println(servletName + "=" + servletClass);
            }
            // 7、处理servlet-mapping标签
            if ("servlet-mapping".equals(element.getName())) {
                // 获取servlet-name
                String servletName = element.element("servlet-name").getText();
                // 获取url-pattern
                String urlPattern = element.element("url-pattern").getText();

                servletMapping.put(urlPattern, servletName);
                System.out.println(urlPattern + "=" + servletName);
            }
        }
        WebApp webApp = new WebApp();
        webApp.setServletInstances(servletInstances);
        webApp.setServletMapping(servletMapping);
        return webApp;
    }
}
