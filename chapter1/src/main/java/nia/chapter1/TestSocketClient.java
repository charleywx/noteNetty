package nia.chapter1;

import java.io.*;
import java.net.Socket;

public class TestSocketClient {
    public static int port = 1888;
    
    public static void main(String[] args) {
        byReadLine();
    }
    
    public static void byReadLine() {
        try {
            Socket socket = new Socket("www.baidu.com", 80);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            // 由系统标准输入设备构造BufferedReader对象
            PrintWriter write = new PrintWriter(socket.getOutputStream());
            write.print("GET / HTTP/1.1\n" +
                    "Host: www.baidu.com\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: zh-CN,zh;q=0.8\n" +
                    "Cookie: BIDUPSID=2DF807BA4251048D64AED7A5307A5178; PSTM=1512013486; BAIDUID=4F408F96A774E2815E8AC9A267286FF9:FG=1; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BD_HOME=0; H_PS_PSSID=1441_21103_25178_20718; BD_UPN=12314753");
            write.close();
            String readline;
            while ((readline = in.readLine()) != null) {
                System.out.println(readline);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

