package cn.enjoy.xxtomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 享学课堂[Roy老师]
 * 类说明：tomcat的服务类
 */
public class XXTomcat {
    private static WebApp webApp;
    public static void main(String[] args) throws Exception {
        // 创建一个线程池，处理每一个连接（注：真实开发中不要这么用）
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 2、启动时加载webapp
        webApp = WebAppLoader.loadWebapp();

        // 1、接收和响应浏览器的请求
        // 1.1、向操作系统声明：帮我开放8080端口，允许远程客户端访问
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("xxtomcat启动了");
        while (true){
            // 1.2、当接收到来自客户端的连接时，创建一个socket来和客户端进行通信
            Socket socket = serverSocket.accept();
            System.out.println("接收请求");
            // 1.3、使用一个线程来处理socket连接
            threadPool.execute(()->{
                try {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    // 3、封装http请求和响应
                    HttpServletRequest req = HttpUtils.createReq(inputStream);
                    HttpServletResponse resp = HttpUtils.createRsp(outputStream);

                    // 4、派发请求: 根据请求的method、url，调用对应的servlet实例的方法
                    dispatch(req,resp);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // 根据请求的method、url，调用对应的servlet实例的方法
    private static void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1、通过req中的url，去webapp里面寻找是否有对应的servletName
        if(webApp.getServletMapping().containsKey(req.getRequestURI())){
            //2、如果找到，则通过servletName获取到servlet实例对象，调用对象的方法
            String servletname = webApp.getServletMapping().get(req.getRequestURI());
            HttpServlet httpServlet = webApp.getServletInstances().get(servletname);
            httpServlet.service(req,resp);
        }

    }

    /*从流中读取信息*/
    private static String getRequstStr(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String info;
        StringBuilder sb = new StringBuilder();
        while ((info = br.readLine()) != null) {
            if (info.length() == 0) break;
            sb.append(info).append("\n");
        }
        return sb.toString();
    }
}
