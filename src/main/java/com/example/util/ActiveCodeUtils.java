package com.example.util;

import java.util.Random;

public class ActiveCodeUtils {

    /**
     * @Title随机生成16为大小写字母+数字
     * @return String
     * */
    private String getCharAndNumr()
    {
        String val = "";
        Random random = new Random();
        for(int i = 0; i < 16; i++)
        {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            }
            else if("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }

    public static String giveCode(){
        String createcode="";//产生的原始激活码
        String changecode="";//改变的激活码
        ActiveCodeUtils activeCodeUtils=new ActiveCodeUtils();
        createcode=activeCodeUtils.getCharAndNumr();//得到一串创建的字符
        for(int i=0;i<createcode.length();){
            changecode+=createcode.substring(i, i+4)+"-";//用for循环给创建的字符串每隔4个加上“-”
            i=i+4;
        }
        changecode=changecode.substring(0, changecode.length()-1);//把最后面的“-”截去
        return changecode;
    }

}
