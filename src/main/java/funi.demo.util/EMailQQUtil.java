package funi.demo.util;

import com.sun.mail.pop3.POP3Folder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件工具类
 * @author zhihuan.niu on 10/10/17.
 */
public class EMailQQUtil extends AbstractEMailUtil{
    private EMailQQUtil(){}
    private static EMailQQUtil instance;
    private static EMailQQUtil getInstance(){
        if(instance==null){
            instance=new EMailQQUtil();
        }
        return instance;
    }
    private static String myEmailAccount = "29155646@qq.com"; //3447003802 niu*2017
    private static String myTelAccount = "13408546119@qq.com";//
    private static String myEmailPassword = "ukrkqlagdhsobgfi";//bysxyrnukzyvcagb
    /*
    邮件创建步骤:
    创建一个邮件对象（MimeMessage）；
    设置发件人，收件人，可选增加多个收件人，抄送人，密送人；
    设置邮件的主题（标题）；
    设置邮件的正文（内容）；
    设置显示的发送时间；
    保存到本地。
    邮箱账号必须要开启 SMTP 服务
    */
    public static void main(String[] args) throws Exception {
        Properties properties=EMailQQUtil.getInstance().getProperties();
        EMailQQUtil.getInstance().sendMessage(properties, myEmailAccount, myEmailPassword, myEmailAccount,
                "牛志欢测试Thread:" + Thread.currentThread().toString(), "我的测试邮件", null);
        //EMailQQUtil.getInstance().sendMessage(myEmailAccount,myEmailPassword,myTelAccount,"牛志欢测试1","我的测试邮件:13408546119@qq.com");
        EMailQQUtil.getInstance().findMimeMessageList(properties,myEmailAccount, myEmailPassword, myEmailAccount);
        //Thread.sleep(1000 * 60 * 5);
    }

    /**
     * 创建参数配置
     * @return
     */
    private Properties getProperties() {
        String emailSMTPHost = "smtp.qq.com";
        Properties props = new Properties();                // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", emailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.pop3.socketFactory.fallback", "true");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.setProperty("mail.pop3.host", "pop.qq.com");

        props.setProperty("mail.store.protocol","imap");
        props.setProperty("mail.imap.host", "imap.qq.com");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.auth.login.disable", "true");

        return props;
    }

}
