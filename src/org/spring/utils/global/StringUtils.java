package org.spring.utils.global;


import org.spring.web.file.PostFormDataBodyListener;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(String str){
        return "".equals(str) || null == str || "null".equals(str);
    }


    //是否是int类型
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //是否是浮点型
    public static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * helloWorld-->hello_world
     */
    public static String transLine(String name) {
        String type = "Lower";
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 循环处理其余字符
            for (int i = 0; i < name.length(); i++) {
                if(i==0&&"Lower".equals(type)) {
                    result.append(name.substring(0, 1).toLowerCase());
                    continue;
                }
                if(i==0) {
                    result.append(name.substring(0, 1).toUpperCase());
                    continue;
                }
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                //其他转化小写
                if("Lower".equals(type)) {
                    result.append(s.toLowerCase());
                    continue;
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 判断是否是布尔类型
     * @param parm
     * @return
     */
    public static boolean isBoolean(String parm) {
        return "true".equalsIgnoreCase(parm) || "false".equalsIgnoreCase(parm);
    }

    /**
     * hello_world-->helloWorld
     * 例如：HELLO_WORLD->HelloWorld
     */
    public static String transHump(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static List<String> readLine(String content) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("") && !line.startsWith("-")) {
                list.add(line);
            }
        }
        return list;
    }

    public static InputStream getInputStream(String content) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(Charset.forName("utf8")));
        return inputStream;
    }

    public static List<String> partitionString(String str,String regex){
        if (!isEmpty(str)){
            String[] partitionStringArray = str.split(regex);
            List<String> list = new ArrayList<>(partitionStringArray.length);
            for (String string : partitionStringArray) {
                list.add(string.trim());
            }
            return list;
        }
        return null;
    }

    /**
     * 将字符串的首字母转大写
     * @param str 需要转换的字符串
     * @return
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs=str.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 将字符串的首字母转小写
     * @param str 需要转换的字符串
     * @return
     */
    public static String lowerFirst(String str) {
        // 同理
        char[] cs=str.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);
    }

    /**
     * 将参数字符串转换成 map（key1=value1&key2=value2）
     * @param parameterString
     * @return
     */
    public static Map<String,Object> getParameterStringTransitionMap(String parameterString){
        if (!isEmpty(parameterString)){
            Map<String,Object> map = new HashMap<>();
            String[] split = parameterString.split("&");
            for (String params : split) {
                String[] parameter = params.split("=");
                String key = parameter[0];
                String value = parameter.length == 2 ? parameter[1] : null;
                if (map.containsKey(key)){
                    Object oldValue = map.get(key);
                    if (List.class.isInstance(oldValue)){
                        List<Object> list = (List<Object>) oldValue;
                        list.add(value);
                    }else {
                        List<Object> list = new ArrayList<>();
                        list.add(oldValue);
                        list.add(value);
                        map.put(key,list);
                    }
                }else {
                    map.put(key,value);
                }
            }
            return map;
        }
        return null;
    }

    public static void getPostFormBodyData(String bodyString, PostFormDataBodyListener listener){
        String regex = "-{28,}[0-9]+";
        boolean isHandlerFile = false;
        String[] bracketContent = bodyString.split(regex);
        for (int i = 1; i < bracketContent.length - 1; i++) {
            String lineString = bracketContent[i];
            String[] childContent = lineString.split(";");
            child:for (int j = 0; j < childContent.length; ++j){
                if (childContent.length == 2){
                    //说明是一个普通参数
                    String data = childContent[1];
                    String[] parameterValue = data.substring(7,data.length()).split("\"");
                    if (listener != null) listener.parameter(i-1,parameterValue[0],parameterValue[1]);
                    break child;
                }else if (childContent.length > 2){
                    if (!isHandlerFile) {
                        /**
                         * 处理文件类型
                         * 会了简化，所有的文件都会合并成一个List到目标方法参数中，不会根据参数字段来区分文件
                         */
                        isHandlerFile = true;
                        if (listener != null) listener.file(i - 1, bodyString);
                        break child;
                    }
                }
            }
        }
    }

    /**
     * 将json转换成Map
     * @param json
     * @return
     */
    public static Map<String,Object> getJsonArrayMap(String json){
        Map<String,Object> map = new HashMap<>();
        String[] jsonArray = replaceCustomCharacter(json, '{', '}').split(",");
        for (int i = 0; i < jsonArray.length; ++i){
            String jsonObject = jsonArray[i];
            jsonObject = jsonObject.trim();
            int endIndex = jsonObject.indexOf(":");
            String key =   replaceCustomCharacter(jsonObject.substring(0,endIndex),'"','"');
            String value = replaceCustomCharacter(jsonObject.substring(endIndex + 1,jsonObject.length()),'"','"').trim();
            if (value.startsWith("{") && value.endsWith("}")){
                //子JSON，在一行上
            }else if (value.startsWith("{")){
                //子JSON，有多行的可能
                String rightString = value + "," + jsonArray[++i];
                while (!rightString.endsWith("}")){
                    rightString += "," + jsonArray[++i];
                }
                map.put(key,getJsonArrayMap(rightString));
            }else {
                map.put(key,value);
            }
        }
        return map;
    }

    public static List<Map<String,Object>> getJsonArrayListByMap(String json){
        List<Map<String,Object>> list = new ArrayList<>();
        String[] jsonArray = replaceCustomCharacter(json, '[', ']').split(",");
        for (int i = 0; i < jsonArray.length; i+=2) {
            String newJson = jsonArray[i] + "," + jsonArray[i+1];
            newJson = newJson.trim();
            list.add(getJsonArrayMap(newJson));
        }
        return list;
    }

    /**
     * 处理字符串两边的特定字符（支持自定义两边字符）
     * @return
     */
    public static String replaceCustomCharacter(String str,char... chars){
        if (chars != null && chars.length >= 2) {
            int start = 0, end = str.length() - 1;
            while ((start < end) && str.charAt(start) == chars[0]) {
                start++;
            }
            while ((start < end) && str.charAt(end) == chars[1]) {
                end--;
            }
            return ((start > 0) || (end < str.length())) ? str.substring(start, end + 1) : str;
        }
        return null;
    }

    /**
     * 将字符串转换成对应的类型
     */
    public static Object basicType(String value){
        if (isEmpty(value)) return null;
        if (isInteger(value)){
            return Integer.valueOf(value);
        }else if (isDouble(value)){
            return Double.valueOf(value);
        }else if (isBoolean(value)){
            return Boolean.valueOf(value);
        }
        return value;
    }

    public static Object basicType(Class<?> clazz,String value){
        if (isEmpty(value)) return null;
        if (ClassUtils.isInt(clazz)){
            return Integer.valueOf(value);
        }else if (ClassUtils.isDouble(clazz)){
            return Double.valueOf(value);
        }else if (ClassUtils.isFloat(clazz)){
            return Float.valueOf(value);
        }else if (ClassUtils.isBoolean(clazz)){
            return Boolean.valueOf(value);
        }
        return value;
    }

    private static Pattern pattern = Pattern.compile("(?<=[A-Z])(?=[A-Z][a-z])|(?<=[^A-Z])(?=[A-Z])");

    public static String convertKey(String userName) {
        Matcher matcher = pattern.matcher(userName);
        return matcher.replaceAll("-$0").toLowerCase();
    }

    /**
     * 处理URI前缀，删除/xx/**中的**
     */
    public static String getURIPrefix(String prefix){
        if (prefix.endsWith("*")){
            //如果是以*号为结尾，则截断该部分
            prefix = prefix.substring(0,prefix.lastIndexOf("/"));
        }
        return "".equals(prefix) ? "/" : prefix;
    }
}
