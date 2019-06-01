package com.mmall.util;

import com.google.common.base.Splitter;
import com.mmall.model.SysUser;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class PasswordUtil {

    public final static String[] word = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] num = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    /*生成一个密码*/
    public static String randomPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(new Date().getTime());
        boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                stringBuffer.append(num[random.nextInt(num.length)]);
            } else {
                stringBuffer.append(word[random.nextInt(word.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) throws Exception {
        List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList("231,3232,21, ,23,21 , , 22");
        for (String string : strings) {
            System.out.println(string);
        }
//        String encrypt = MD5Util.encrypt("123456");
//        System.out.println(encrypt);
//        System.out.println(randomPassword());
//        Thread.sleep(100);
//        System.out.println(randomPassword());
//        Thread.sleep(100);
//        System.out.println(randomPassword());
    }
}
