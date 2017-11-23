package funi.demo.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类网易163
 * @author zhihuan.niu on 10/10/17.
 */
public class EMail163Util extends AbstractEMailUtil{
    private EMail163Util(){}
    private static EMail163Util instance;
    private static EMail163Util getInstance(){
        if(instance==null){
            instance=new EMail163Util();
        }
        return instance;
    }

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
        String myEmailAccount = "niuzhihuan@163.com";
        String myEmailPassword = "niu2017";
        Properties properties=EMail163Util.getInstance().getProperties();
        String content="我的测试邮件"+new Date().toString();
        String filePath=System.getProperty("user.dir");
        filePath=filePath.concat(File.separator).concat("src/main/java/funi.demo.util/EMail163Util.java");
        EMail163Util.getInstance().sendMessage(properties,myEmailAccount,myEmailPassword,myEmailAccount,"牛志欢测试1",content,filePath);
        MailMessage message=EMail163Util.getInstance().findMimeMessageList(properties,myEmailAccount,myEmailPassword,myEmailAccount);
    }

    /**
     * 创建参数配置
     * @return
     */
    private Properties getProperties() {
        Properties props = new Properties();                // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", "smtp.163.com");   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.pop3.host", "pop.163.com");

        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.163.com");
        props.setProperty("mail.imap.port", "143");
        props.setProperty("mail.imap.auth.login.disable", "true");
        return props;
    }
}
