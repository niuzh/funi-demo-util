package funi.demo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 进程工具
 * @author zhihuan.niu on 10/13/17.
 */
public class ProcessUtil {

    public static void main(String[] argv) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("halt -p");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void process(){
        InputStream in = null;
        try {
            Process pro = Runtime.getRuntime().exec(new String[]{"sh",
                    "/home/niu/Documents/IdeaProjects/funi-demo-util/target/classes/run.sh"});
            pro.waitFor();
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String result = read.readLine();
            System.out.println("INFO:"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
