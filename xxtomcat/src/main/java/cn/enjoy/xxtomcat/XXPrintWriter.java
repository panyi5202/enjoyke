package cn.enjoy.xxtomcat;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author 享学课堂[Roy老师]
 */
public class XXPrintWriter extends PrintWriter {
    public XXPrintWriter(OutputStream out) {
        super(out);
    }

    @Override
    public void write(String content) {
        String rspHeader="HTTP/1.1  200  OK\n" +
                "Content-Type:text/html;charset=utf-8\n" +
                "Content-Length: "+content.length()+"\n";
        super.write(rspHeader);
        super.write("\n");
        super.write(content);
        super.flush();
    }
}
