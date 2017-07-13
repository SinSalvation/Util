package org.util;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jixu_m on 2017/7/13.
 */
public class SensitiveUtil {
    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);
    static List<Character> list = new ArrayList<>();
    static Map<Character, Object> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        createTable(args[0]);
        System.out.println("建表时间：" + (System.nanoTime() - startTime));
        Scanner scanner = new Scanner(System.in);
        String temp;
        while(!(temp = scanner.nextLine()).equals(-1)){
            startTime = System.nanoTime();
            System.out.println("查询结果：" + isMaskWord(temp));
            System.out.println("查询时间：" + (System.nanoTime() - startTime));
        }
    }

    public static void createTable(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String content;
        char a = '0';
        while ((content = bufferedReader.readLine()) != null) {
            content = format(content);
            char[] chars = content.toCharArray();
            if (chars.length <= 6 && chars.length >= 1) {
                int i = 0;
                Map<Character, Object> temp = map;
                for (char c : chars) {
                    a = c;
                    if (temp.containsKey(c)) {
                        temp = (Map<Character, Object>) temp.get(c);
                    } else {
                        temp.put(c, new HashMap<Character, Object>());
                    }
                    i++;
                }
                if (i == 1) {
                    if (list.size() == 0 || list.get(list.size() - 1) < a) {
                        list.add(a);
                    } else {
                        for (int n = 0; n < list.size() - 1; n++) {
                            if (a > list.get(n) && a < list.get(n + 1)) {
                                list.add(n + 1, a);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isChinese(char c) {
        Matcher matcher = pat.matcher(String.valueOf(c));
        boolean flg = false;
        if (matcher.find())
            flg = true;
        return flg;
    }

    public static String format(String s) {
        String temp = "";
        for (char c : s.toCharArray()) {
            if (isChinese(c)) {
                temp += c;
            }
        }
        return temp;
    }

    public static boolean isMaskWord(String s) {
        s = format(s);
        char[] chars = s.toCharArray();
        if (chars.length > 6 || chars.length < 1) {
            return true;
        }
        for (char c : chars) {
            if (isChinese(c)) {
                int min = 0;
                int max = list.size();
                while (min < list.size() - 1) {
                    int now = (max + min) / 2;
                    if (c == list.get(now)) {
                        return true;
                    } else if (c < list.get(now)) {
                        max = now;
                    } else if (c > list.get(now)) {
                        min = now;
                    }
                    if (max == min && c == list.get(max)) {
                        return true;
                    }
                    if (max == 0 || max - min == 1) {
                        break;
                    }
                }
            }
        }
        if (chars.length > 1) {
            Map<Character, Object> temp = map;
            int i = 0;
            for (int n=0;n<chars.length;) {
                if (i < 2) {
                    if (temp.containsKey(chars[n])) {
                        temp = (Map<Character, Object>) temp.get(chars[n]);
                        i++;
                        n++;
                    } else {
                        i = 0;
                        temp = map;
                        continue;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
