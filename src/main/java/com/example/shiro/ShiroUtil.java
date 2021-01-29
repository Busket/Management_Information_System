package com.example.shiro;


import com.example.util.ConstantUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @version 1.0
 * @date 2020/5/30 11:07
 */

public class ShiroUtil {
    /**
     * 传入一个明文返回一个加密之后的密文
     */
    public static  String getciphertext(String phertext){
        String ciphertext = DigestUtils.md5DigestAsHex(phertext.getBytes());
        return ciphertext;
    }

    public static String encryptionBySalt(String salt,String message)
    {

        String algorithmName= ConstantUtils.algorithmName;
        int hashIteration=ConstantUtils.hashIterations;
                                        //四个参数：加密算法，明文，盐值，加密次数。
        SimpleHash simpleHash=new SimpleHash(algorithmName,message,salt,hashIteration);
        return simpleHash.toString();

    }
    public static void main(String[] args) {
        String message="hhh hhh";
        String s = getciphertext(message);
        System.out.println(s);
        //shiro加密
        //String algorithmName, Object source, Object salt, int hashIterations
        /*String algorithmName="MD5";
        String source="I love you";
       /* String salt=UUID.randomUUID().toString();
        int hashIterations=1000;
        SimpleHash simpleHash= new SimpleHash(algorithmName,  source,  salt,  hashIterations);
        System.out.println("加密之后的密文："+ simpleHash);
        System.out.println("加密时用的盐："+salt);*/

        String salt= UUID.randomUUID().toString();
        String en=encryptionBySalt(salt,message);
        System.out.println(en);
    }
}
