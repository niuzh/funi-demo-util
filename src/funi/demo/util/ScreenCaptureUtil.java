package funi.demo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

/**
 * 屏幕
 * @author zhihuan.niu on 10/10/17.
 */
public class ScreenCaptureUtil {
    public static void main(String[] args) throws Exception {
        String userdir = System.getProperty("user.dir");
        if(args.length>0){
            userdir=args[0].toString().trim();
        }
        System.out.println(userdir);
        System.out.println("Over");
        BufferedImage fullScreenImage=ScreenCaptureUtil.getFullScreenImage();
        File file=new File(userdir+ File.separator+new Date().getTime()+".jpeg");
        System.out.println(file.getPath());
        fullScreenImage.createGraphics().drawString(new Date().toString(),10,10);
        ImageIO.write(fullScreenImage, "JPEG", file);
    }

    /**
     * 截取屏幕图片
     * @return
     */
    public static BufferedImage getFullScreenImage(){
        Robot robot=null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Internal Error: " + e);
            e.printStackTrace();
        }
        BufferedImage fullScreenImage = robot.createScreenCapture(new Rectangle(Toolkit
                .getDefaultToolkit().getScreenSize()));
        return fullScreenImage;
    }
}
