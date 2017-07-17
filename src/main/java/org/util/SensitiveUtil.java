package org.util;

import java.io.*;
import java.util.*;

/**
 * Created by jixu_m on 2017/7/6.
 */
public class SensitiveUtil {
    static char startChinese = "\u4e00".charAt(0);
    static char endChinese = "\u9fcb".charAt(0);
    static char startBigEnglish = "\u0041".charAt(0);
    static char endBigEnglish = "\u005a".charAt(0);
    static char startSmallEnglish = "\u0061".charAt(0);
    static char endSmallEnglish = "\u007a".charAt(0);
    static List<MaskWord> maskWords = new ArrayList<>();
    static List<MaskWord> list1 = new ArrayList<>();
    static List<MaskWord> list2 = new ArrayList<>();
    static List<MaskWord> list3 = new ArrayList<>();
    static List<MaskWord> list4 = new ArrayList<>();
    static List<MaskWord> list5 = new ArrayList<>();
    static List<MaskWord> list6 = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        createTable(args[0]);
        System.out.println("建表时间：" + (System.nanoTime() - startTime));
        Scanner scanner = new Scanner(System.in);
        String temp;
        while(!(temp = scanner.nextLine()).equals(-1)){
            startTime = System.nanoTime();
            System.out.println("查询结果：" + isMaskWord(temp) + "\n查询时间：" + (System.nanoTime() - startTime));
        }
    }

    public static void createTable(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String content;
        MaskWord maskWord;
        while ((content = bufferedReader.readLine()) != null) {
            content = format(content);
            char[] chars = content.toCharArray();
            if (chars.length <= 6 && chars.length >= 1) {
                int n = 0;
                List<MaskWord> tempList = maskWords;
                for(char c : chars){
                    maskWord = new MaskWord();
                    maskWord.setValue(c);
                    maskWord.setList(new ArrayList<MaskWord>());
                    if(chars.length == 1){
                        list1.add(maskWord);
                    }
                    if (find(c,tempList) == null) {
                        tempList.add(maskWord);
                    } else {
                        for (int i = 0; i < tempList.size() - 1; i++) {
                            if (c > tempList.get(i).getValue() && c < tempList.get(i + 1).getValue()) {
                                n = i + 1;
                                tempList.add(i + 1, maskWord);
                                break;
                            }
                        }
                    }
                    tempList = tempList.get(n).getList();
                }
            }
        }
    }

    public static boolean isChineseOrEnglish(char c) {
        if((c>=startChinese && c<=endChinese)){
            return true;
        }
        return false;
    }

    public static String format(String s) {
        String temp = "";
        for (char c : s.toCharArray()) {
            if (isChineseOrEnglish(c)) {
                temp += c;
            }
        }
        return temp;
    }

    public static boolean isMaskWord(String s) {
        s = format(s);
        char[] chars = s.toCharArray();
        if (chars.length > 6|| chars.length < 1) {
            return true;
        }
        for (char c : chars) {
            if(find(c,list1)!=null){
                return true;
            }
        }
        if (chars.length > 1) {
            List<MaskWord> tempList = maskWords;
            int i = 0;
            for (int n = 0; n < chars.length; n++) {
                MaskWord maskWord = find(chars[n],tempList);
                if (maskWord != null) {
                    tempList = maskWord.getList();
                    i++;
                    if(i > 0 && tempList.size() == 0){
                        return true;
                    }
                } else {
                    if (i > 0) {
                        n--;
                    }
                    i = 0;
                    tempList = maskWords;
                    continue;
                }
            }
        }
        return false;
    }

    public static MaskWord find(char c,List<MaskWord> tempList){
        int min = 0;
        int max = tempList.size();
        if(max != 0){
            if(tempList.get(0).getValue() == c){
                return tempList.get(0);
            }
        }
        while (min < tempList.size() - 1) {
            int now = (max + min) / 2;
            if (c == tempList.get(now).getValue()) {
                return tempList.get(now);
            } else if (c < tempList.get(now).getValue()) {
                max = now;
            } else if (c > tempList.get(now).getValue()) {
                min = now;
            }
            if (max == min && c == tempList.get(max).getValue()) {
                return tempList.get(now);
            }
            if (max == 0 || max - min == 1) {
                break;
            }
        }
        return null;
    }

    public static List<MaskWord> findList(int i){
        switch (i){
            case 1 :{
                return list1;
            }
            case 2 :{
                return list2;
            }
            case 3 :{
                return list3;
            }
            case 4 :{
                return list4;
            }
            case 5 :{
                return list5;
            }
            case 6 :{
                return list6;
            }
            default:{
                return maskWords;
            }
        }
    }
}