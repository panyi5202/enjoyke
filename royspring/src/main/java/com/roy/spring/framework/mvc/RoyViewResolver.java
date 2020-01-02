package com.roy.spring.framework.mvc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.RandomAccess;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roy
 * 将静态文件中的参数表达式解析成对应的值
 */
public class RoyViewResolver {
    private String viewName;
    private File templateFile;

    public RoyViewResolver(String viewName, File tempFile) {
        this.viewName = viewName;
        this.templateFile = tempFile;
    }

    public String resolverView(RoyModeAndView mv) throws IOException {
        RandomAccessFile ra = new RandomAccessFile(this.templateFile, "r");
        StringBuilder sb = new StringBuilder();

        String line = null;
        while (null != (line = ra.readLine())){
            Matcher matcher = matcher(line);
            while (matcher.find()){
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    // 把模板中的参数名称取出来
                    String paramName = matcher.group(i);
                    Object paramValue = mv.getModel().get(paramName);
                    if (paramValue != null){
                        line = line.replaceAll("\\$\\{"+paramName+"\\}",paramValue.toString());
                    }
                }
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private Matcher matcher(String str){
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        String line = "aaddd${dd}32332";
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()){
            System.out.println(matcher.groupCount());
        }
    }
}
