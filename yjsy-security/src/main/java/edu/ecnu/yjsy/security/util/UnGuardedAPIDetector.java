package edu.ecnu.yjsy.security.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

/**
 * FIXME
 * <p>
 * 类名不好，建议更换
 * <p>
 * 通过读取访问<code>Spring Boot Actuator's /mappings</code>获得后端服务接口，
 * 并通过与数据库中对应的权限管理表进行比对，以确认哪个接口的权限控制可能被忽略了。
 *
 * @author xiafan
 */
public class UnGuardedAPIDetector {

    private static final Pattern API_PATTERN = Pattern
            .compile("\\{\\[([^\\|\\]]+)");

    private static final String[] methods = new String[] { "GET", "POST", "PUT",
            "DELETE", "HEAD" };

    public static List<String> parseEndPoints(String field) {
        List<String> ret = new ArrayList<String>();
        if (!field.contains("{")) {
            for (String method : methods) {
                ret.add(String.format("%s,%s,", field, method));
            }
        } else {
            Matcher matcher = API_PATTERN.matcher(field);
            if (matcher.find()) {
                String api = matcher.group(1);
                String methodField = field.substring(
                        field.indexOf("methods") + "methods".length());
                for (String method : methods) {
                    if (methodField.contains(method)) {
                        ret.add(String.format("%s,%s,", api, method));
                    }
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(
                    new FileInputStream("/home/xiafan/文档/urlmapping.txt"));
            StringBuffer buf = new StringBuffer();
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream("/home/xiafan/文档/urlmapping.csv"));
            while (scanner.hasNext())
                buf.append(scanner.next());
            JSONObject jsonObject = JSONObject.fromObject(buf.toString());
            for (Object key : jsonObject.keySet()) {
                for (String endpoint : parseEndPoints(key.toString())) {
                    writer.write(endpoint + "\n");
                }
            }
            scanner.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
