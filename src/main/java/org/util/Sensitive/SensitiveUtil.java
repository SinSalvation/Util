package org.util.Sensitive;

import java.io.*;
import java.util.*;

/**
 * Created by jixu_m on 2017/7/6.
 */
public class SensitiveUtil {
    private static char startChinese = "\u4e00".charAt(0);
    private static char endChinese = "\u9fcb".charAt(0);
    private static char startBigEnglish = "\u0041".charAt(0);
    private static char endBigEnglish = "\u005a".charAt(0);
    private static char startSmallEnglish = "\u0061".charAt(0);
    private static char endSmallEnglish = "\u007a".charAt(0);
    private static char point = "\u002E".charAt(0);
    private static char startNumber = "\u0030".charAt(0);
    private static char endNumber = "\u0039".charAt(0);
    private static List<MaskWord> maskWords = new ArrayList<>();
    private static List<MaskWord> oneWord = new ArrayList<>();

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
            //content = format(content);
            char[] chars = content.toCharArray();
            byte[] bytes = content.getBytes();
            boolean flag;
            if (bytes.length <= 18 && bytes.length >= 1) {
                List<MaskWord> tempList = maskWords;
                int a = 0;
                for(char c : chars){
                    if(chars.length == 1){
                        continue;
                    }
                    a++;
                    flag = true;
                    maskWord = new MaskWord();
                    maskWord.setValue(c);
                    maskWord.setStop(false);
                    if (a == chars.length) {
                        maskWord.setStop(true);
                    }
                    maskWord.setList(new ArrayList<MaskWord>());
//                if(chars.length == 1 && isChinese(c)){
//                    if (oneWord.size() == 0) {
//                        oneWord.add(maskWord);
//                    }else if(oneWord.size() == 1){
//                        if(c > oneWord.get(0).getValue()){
//                            oneWord.add(maskWord);
//                        } else {
//                            oneWord.add(0,maskWord);
//                        }
//                    }else {
//                        if (c < oneWord.get(0).getValue()) {
//                            oneWord.add(0, maskWord);
//                        } else if (c > oneWord.get(oneWord.size() - 1).getValue()) {
//                            oneWord.add(maskWord);
//                        } else {
//                            for (int i = 0; i < oneWord.size() - 1; i++) {
//                                if (c > oneWord.get(i).getValue() && c < oneWord.get(i + 1).getValue()) {
//                                    oneWord.add(i + 1, maskWord);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
                    for (MaskWord m : tempList) {
                        if (c == m.getValue()) {
                            tempList = m.getList();
                            if (a == chars.length) {
                                m.setStop(true);
                            }
                            flag = false;
                        }
                    }
                    if(flag) {
                        if (tempList.size() == 0) {
                            tempList.add(maskWord);
                        }else if(tempList.size() == 1) {
                            if (c > tempList.get(0).getValue()) {
                                tempList.add(maskWord);
                            } else {
                                tempList.add(0, maskWord);
                            }
                        } else {
                            if (c < tempList.get(0).getValue()) {
                                tempList.add(0, maskWord);
                            } else if (c > tempList.get(tempList.size() - 1).getValue()) {
                                tempList.add(maskWord);
                            } else {
                                for (int i = 0; i < tempList.size() - 1; i++) {
                                    if (c > tempList.get(i).getValue() && c < tempList.get(i + 1).getValue()) {
                                        tempList.add(i + 1, maskWord);
                                        break;
                                    }
                                }
                            }
                        }
                        tempList = maskWord.getList();
                    }
                }
            }
        }
    }

    public static boolean isChineseOrEnglish(char c) {
        if((c>=startChinese && c<=endChinese)||(c>=startBigEnglish && c<=endBigEnglish)||(c>=startSmallEnglish && c<=endSmallEnglish)||(c>=startNumber && c<=endNumber)){
            return true;
        }
        return false;
    }

    public static boolean isChinese(char c){
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
        //s = format(s);
        char[] chars = s.toCharArray();
        byte[] bytes = s.getBytes();
        if (bytes.length > 18|| bytes.length < 1) {
            return true;
        }
//        for (char c : chars) {
//            if(isChinese(c)){
//                if(find(c,oneWord)!=null){
//                    return true;
//                }
//            }
//        }
        if (chars.length > 1) {
            List<MaskWord> tempList = maskWords;
            int i = 0;
            for (int n = 0; n < chars.length; n++) {
                MaskWord maskWord = find(chars[n],tempList);
                if (maskWord != null) {
                    tempList = maskWord.getList();
                    i++;
                    if((i > 1 && maskWord.isStop())){
                        return true;
                    }
                } else {
                    if (i > 0) {
                        n=n-i;
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
}