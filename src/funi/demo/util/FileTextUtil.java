package funi.demo.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhihuan.niu on 10/19/17.
 */
public class FileTextUtil {
    private FileTextUtil(){}
    private static FileTextUtil instance;
    private static FileTextUtil getInstance(){
        if(instance==null){
            instance=new FileTextUtil();
        }
        return instance;
    }

    public static void main(String[] args) {
        String filePath="/home/niu/Downloads/莽荒纪.txt";
        FileTextUtil.getInstance().convert(filePath,"gbk","utf8");
        /*String filePath="/home/niu/Downloads/test.txt";
        FileTextUtil.getInstance().convert(filePath,"utf8","utf8");*/

    }

    /**
     *
     * @param filePath
     * @param sourceEncodingName gbk utf8
     * @param encoding
     */
    public void convert(String filePath,String sourceEncodingName,String encoding){
        try{
            File sourceFile = new File(filePath);
            filePath=filePath.substring(0,filePath.lastIndexOf(".")).concat(encoding)
                    .concat(filePath.substring(filePath.lastIndexOf(".")));
            File targetF = new File(filePath);
            //targetF.deleteOnExit();
            targetF.createNewFile();
            if(sourceFile.isFile()&&sourceFile.exists())
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(sourceFile),sourceEncodingName);
                OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(targetF),encoding);
                BufferedWriter writer=new BufferedWriter(write);
                BufferedReader reader=new BufferedReader(read);
                String line;
                String preLine="";
                int i=1;
                while ((line = reader.readLine()) != null){
                    line=dealStr(line);
                    if(preLine.trim().equals(line.trim())&&line.contains("第")&&line.contains("章")){
                        line="";
                    }
                    if(!line.trim().equals("")) {
                        writer.write(line);
                        writer.write("\n");
                        //System.out.println(line);
                    }
                    String chapter=FileTextUtil.getInstance().getChapter(i);
                    if(line.contains(chapter)){
                        System.out.println(line);
                        i++;
                    }
                    preLine=line;
                }
                read.close();
                writer.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private String dealStr(String str){
        List<String> list=new ArrayList<String>();
        list.add("==========================================================");
        list.add("正文");
        list.add("《 笔下文学 》整理收藏 http://www.bxwx8.org");
        list.add("<a href=");
        list.add("\"http://www.\"");
        list.add("target=\"_blank\">");
        list.add("http://www.");
        list.add("</a>");
        list.add("www.”");
        list.add("www.");
        list.add("[网]");
        list.add("wwW.WenXueMi.coM");
        list.add("……");
        list.add("——");
        list.add("~~~~~~~");
        list.add("※");
        list.add("（拜求推荐票）");
        list.add("wenxuemi.com");
        list.add("（第三更）");
        list.add("点击、推荐票，都砸来吧！");
        list.add("(全书完)");
        list.add("————求推荐票~~~~~");
        list.add("更多精校小说尽在知轩藏书下载：http://www.zxcs8.com/");
        list.add("(求推荐pp)");
        list.add("二更到，晚上还一更");
        list.add("转载自");
        list.add("二更到，求票票");
        list.add("三更，一更到");
        list.add("[bookid=1736480,bookname=《未来手机》][bookid=1549228,bookname=《重生之再世为仙》]");
        list.add("------跪求月票------");
        list.add("(文字暂缺)");
        list.add("()");
        list.add("（）");
        list.add("[]");
        for (String s:list) {
            str=str.toLowerCase().replace(s.toLowerCase(),"");
            if(str.contains("第")&&str.contains("章")){
                str=str.replace("]","").replace("精彩","");
            }
        }
        if(str.replace("　　","").length()==0){
            return "";
        }
        if(str.trim().replace("-","").replace("跪求月票","").replace("求求大家投出月票吧","").length()==0){
            return "";
        }
        list.clear();
        list.add("ps");
        list.add("【ps：");
        list.add("大家的月票");
        list.add("三章完毕");
        for (String s:list){
            String strTrim=str.replace("　　","").trim();
            if(strTrim.startsWith(s)){
                return "";
            }
            if(strTrim.startsWith("【")&&strTrim.endsWith("】")){
                return "";
            }
        }
        Map<String,String> map=new HashMap<String, String>();
        map.put("ooo","000");
        map.put("x&#236;ng","性");
        map.put("s&#232;","色");
        map.put("l&#249;","露");
        map.put("c&#224;o","操");
        map.put("过jī","过激");
        map.put("我***","我他妈的");
        for (Map.Entry<String,String> entry:map.entrySet()){
            str.replace(entry.getKey(),entry.getValue());
        }
        return str;
    }

    private String getChapter(Integer i){
        /**
         * 中文数字正写
         */
        char[] cnNumbers = { ' ', '一', '二', '三', '四', '五',
                '六', '七', '八', '九' };
        char[] stdNumbers = { '0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9' };
        String chapter="第";
//十  百 千
        if(i.toString().length()==1){
            chapter+=cnNumbers[i];
        }if(i.toString().length()==2){
            char s1=cnNumbers[Integer.parseInt(i.toString().substring(0,1))];
            char s2=cnNumbers[Integer.parseInt(i.toString().substring(1,2))];
            if(s1=='一' &&s2==' '){
                chapter+="十";
            }else if(s1=='一' &&s2!=' '){
                chapter+="十"+s2;
            }else if(s2==' '){
                chapter+=s1+"十";
            }else {
                chapter += s1 + "十" + s2;
            }
        }if(i.toString().length()==3){
            char s1=cnNumbers[Integer.parseInt(i.toString().substring(0,1))];
            char s2=cnNumbers[Integer.parseInt(i.toString().substring(1,2))];
            char s3=cnNumbers[Integer.parseInt(i.toString().substring(2,3))];
            if(s2==' '&&s3==' '){
                chapter+=s1+"百";
            }if(s2==' '&&s3!=' '){
                chapter+=s1+"百零"+s3;
            }if(s2!=' '&&s3==' '){
                chapter+=s1+"百"+s2+"十";
            }if(s2!=' '&&s3!=' '){
                chapter+=s1+"百"+s2+"十"+s3;
            }
        }
     return chapter.trim()+"章";
    }
}
