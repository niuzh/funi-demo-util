package funi.demo.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件工具类
 * @author zhihuan.niu on 10/10/17.
 */
public class AbstractEMailUtil {

    /**
     *
     * @param sendProps 发件人邮箱Properties
     * @param sendMail 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
     * @param sendMailPassword 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
     * @param receiveMail 收件人邮箱（替换为自己知道的有效邮箱）
     * @param subject 邮箱主题
     * @param content 邮件内容
     * @throws Exception
     */
    public void sendMessage(Properties sendProps,String sendMail,String sendMailPassword, String receiveMail,String subject, String content,String filePath) throws Exception {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = sendProps;

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session= Session.getDefaultInstance(props); // 根据参数配置，创建会话对象（为了发送邮件准备的）
        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session,subject, sendMail, receiveMail,content,filePath);
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        //
        //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
        //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
        //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
        //
        //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
        //           (1) 邮箱没有开启 SMTP 服务;
        //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
        //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
        //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
        //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
        //
        //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
        transport.connect(sendMail, sendMailPassword);
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
        // 7. 关闭连接
        transport.close();
    }

    /**
     * 创建参数配置
     * @param emailSMTPHost 发件人的邮箱的 SMTP 服务器地址
     * @return
     */
    private Properties getProperties(String emailSMTPHost) {
        Properties props = new Properties();                // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", emailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        return props;
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    protected MimeMessage createMimeMessage(Session session,String subject, String sendMail, String receiveMail,String content,String filePath) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        //sendMail="funi@funi.com";
        message.setFrom(new InternetAddress(sendMail, "", "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "用户", "UTF-8"));
        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(subject, "UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        //整封邮件的MINE消息体
        MimeMultipart msgMultipart = new MimeMultipart("mixed");//混合的组合关系
        message.setContent(msgMultipart);
        //附件
        if(filePath!=null&&filePath!="") {
            File file = new File(filePath);
            if(file.exists()) {
                MimeBodyPart attch = new MimeBodyPart();
                msgMultipart.addBodyPart(attch);
                //把文件，添加到附件1中
                //数据源
                DataSource ds = new FileDataSource(file);
                //数据处理器
                DataHandler dh = new DataHandler(ds);
                //设置第一个附件的数据
                attch.setDataHandler(dh);
                //设置第一个附件的文件名
                attch.setFileName(file.getName());
            }
        }
        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        //正文内容
        //正文（图片和文字部分）
        MimeMultipart bodyMultipart  = new MimeMultipart("related");
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        //设置内容为正文
        mimeBodyPart.setContent(bodyMultipart);
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(content,"text/html;charset=UTF-8");
        //设置邮件的MINE消息体
        bodyMultipart.addBodyPart(htmlPart);
        msgMultipart.addBodyPart(mimeBodyPart);
        // 7. 保存设置
        message.saveChanges();
        return message;
    }

    /**
     * 收取发送者的邮件并删除
     * @param receiveProps  收件者邮箱Properties
     * @param receiveMail 收件者邮箱
     * @param receiveMailPassword 收件者密码
     * @param sendMail 发送者邮箱
     * @return
     */
    public MailMessage findMimeMessageList(Properties receiveProps,String receiveMail,String receiveMailPassword,String sendMail) throws Exception {
        MailMessage mailMessage=null;
        Properties props=receiveProps;
        Session session = Session.getInstance(props);
        Store store = session.getStore("pop3");
        store.connect(receiveMail,receiveMailPassword);
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_WRITE);
        Integer getMessageCount =folder.getMessageCount();
        System.out.println("getMessageCount："+getMessageCount);
        for (int i=getMessageCount;i>1;i--){
            MimeMessage mimeMessage=(MimeMessage) folder.getMessage(i);
            String formMail=((InternetAddress)mimeMessage.getFrom()[0]).getAddress();
            System.out.println(formMail);
            if(formMail.equals(sendMail)) {
                mailMessage=new MailMessage(mimeMessage);
                mimeMessage.setFlag(Flags.Flag.DELETED, true);
                System.out.println(mailMessage.toString());
                //mailMessage.saveAttach();
                break;
            }
        }
        folder.close(true);
        store.close();
        return mailMessage;
    }

    class MailMessage{
        public MailMessage(MimeMessage mimeMessage) throws Exception {
            sendDate=mimeMessage.getSentDate();
            subject=mimeMessage.getSubject();
            formMail=((InternetAddress)mimeMessage.getFrom()[0]).getAddress();
            getAttach(mimeMessage);
        }
        private Date sendDate;
        private String subject;
        private String content;
        private String formMail;
        private String attachName;
        private byte[] attachFile;
        public Date getSendDate() {
            return sendDate;
        }

        public void setSendDate(Date sendDate) {
            this.sendDate = sendDate;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFormMail() {
            return formMail;
        }

        public void setFormMail(String formMail) {
            this.formMail = formMail;
        }

        public String getAttachName() {
            return attachName;
        }

        public void setAttachName(String attachName) {
            this.attachName = attachName;
        }

        public byte[] getAttachFile() {
            return attachFile;
        }

        public void setAttachFile(byte[] attachFile) {
            this.attachFile = attachFile;
        }

        @Override
        public String toString(){
            return "sendDate"+sendDate+" subject:"+subject+" content:"+content;
        }

        /**
         * 判断此邮件是否包含附件
         */
        private void getAttach(Part part) throws Exception {
            if (part.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) part.getContent();
                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart mpart = mp.getBodyPart(i);
                    String disposition = mpart.getDisposition();
                    if ((disposition != null) &&(disposition.equals(Part.ATTACHMENT) || disposition.equals(Part.INLINE))){
                        String filename=mpart.getFileName();
                        if(filename==null)continue;
                        attachName=javax.mail.internet.MimeUtility.decodeText(filename);
                        InputStream in = part.getInputStream();// 打开附件的输入流
                        //保存附件
                        String filePath=System.getProperty("user.dir");
                        filePath=filePath.concat(File.separator).concat(this.attachName);
                        //new File(filePath).deleteOnExit();
                        System.out.println(filePath);
                        OutputStreamWriter write=new OutputStreamWriter(new FileOutputStream(new File(filePath)));
                        BufferedWriter writer=new BufferedWriter(write);
                        byte[] content = new byte[255];
                        int data;
                        while ((data=in.read(content))!=-1){
                            writer.write(data);
                        }
                        writer.close();
                        in.close();
                    }
                    if(mpart.isMimeType("text/html")) {
                        content=mpart.getContent().toString();
                    }
                    getAttach(mpart);
                }
            } else if (part.isMimeType("message/rfc822")) {
                getAttach((Part) part.getContent());
            }
        }

        private void saveAttach() throws Exception{
            if(getAttachName()==null)return;
            //保存附件
            String filePath=System.getProperty("user.dir");
            filePath=filePath.concat(File.separator).concat(this.attachName);
            File file=new File(filePath);
            System.out.println(filePath);
            BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(attachFile);
            outputStream.flush();
            outputStream.close();
        }
    }
}
