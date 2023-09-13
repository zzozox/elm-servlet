package ynu.edu.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理中文编码问题
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");//表明返回的是一个json
        //获取请求路径
        String path= req.getServletPath();
        //获取类名和方法名
        String className=path.substring(1,path.lastIndexOf("/"));
        String methodName=path.substring(path.lastIndexOf("/")+1);

        className="ynu.edu.controller."+className;

        PrintWriter out=null;

        try{
            //获取controller类的类型
            Class clazz=Class.forName(className);
            //创建controller对象
            Object contrller=clazz.getConstructor().newInstance();
            //获取方法
            Method method=clazz.getMethod(methodName,new Class[]{HttpServletRequest.class});
            //调用方法返回JAV对象
            Object result=method.invoke(contrller,new Object[]{req});
            //把JAVA对象转换为JSON
            out=resp.getWriter();
            ObjectMapper om=new ObjectMapper();
            out.print(om.writeValueAsString(result));
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("application/json;charset=utf-8");
            out=resp.getWriter();
            out.println("<html><body>");
            out.println("<h1>"+"请求出错，当前请求路径为："+path+"</h1>");
            out.println("<h1>"+"类名："+className+"；方法名："+methodName+"</h1>");
            out.println("<html></body>");
        }
        finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
