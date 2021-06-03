package skill;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @Author liu.teng
 * @Date 2021/4/14 10:28
 * @Version 1.0
 */
public class CommonTools {
    /**
     * 将异常堆栈转换为字符串
     * @param e 异常
     * @return String
     */
    public static String getStackTrace(Throwable e) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }
}
