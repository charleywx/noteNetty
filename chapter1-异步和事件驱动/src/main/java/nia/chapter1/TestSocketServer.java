package nia.chapter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestSocketServer {
    public static int port = 1888;
    
    public static void main(String[] args) {
        byReadLine();
    }
    
    public static void byReadLine() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            String request;
//            while ((request = in.readLine()) != null) {
//                System.out.println(request);
//                if ("Done".equals(request)) {
//                    break;
//                }
//            }
    
//            out.println("HTTP/1.1 200 OK\n" +
//                    "Bdpagetype: 1\n" +
//                    "Bdqid: 0xf8ba083400009f30\n" +
//                    "Bduserid: 0\n" +
//                    "Cache-Control: private\n" +
//                    "Connection: Keep-Alive\n" +
//                    "Content-Encoding: gzip\n" +
//                    "Content-Type: text/html; charset=utf-8\n" +
//                    "Cxy_all: baidu+40ffe26c40533801142d31493cabf8c5\n" +
//                    "Date: Thu, 30 Nov 2017 06:28:05 GMT\n" +
//                    "Expires: Thu, 30 Nov 2017 06:27:19 GMT\n" +
//                    "Server: BWS/1.1\n" +
//                    "Set-Cookie: BDSVRTM=0; path=/\n" +
//                    "Set-Cookie: BD_HOME=0; path=/\n" +
//                    "Set-Cookie: H_PS_PSSID=1441_21103_25178_20718; path=/; domain=.baidu.com\n" +
//                    "Strict-Transport-Security: max-age=172800\n" +
//                    "Vary: Accept-Encoding\n" +
//                    "X-Powered-By: HPHP\n" +
//                    "X-Ua-Compatible: IE=Edge,chrome=1\n" +
//                    "Transfer-Encoding: chunked");
            
            out.print("HTTP/1.1 200 OK\n" +
                    "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
                    "Server: Apache\n" +
                    "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
                    "ETag: \"34aa387-d-1568eb00\"\n" +
                    "Accept-Ranges: bytes\n" +
                    "Content-Length: 51\n" +
                    "Vary: Accept-Encoding\n" +
                    "Content-Type: text/plain\n" +
                    "http><body><h1>你好！</h1></body></http>");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    
//    Connected to the target VM, address: '127.0.0.1:51298', transport: 'socket'
//        GET / HTTP/1.1
//        Host: 127.0.0.1:1888
//        Connection: keep-alive
//        Cache-Control: max-age=0
//        Upgrade-Insecure-Requests: 1
//        User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36
//        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//Accept-Encoding: gzip, deflate, br
//Accept-Language: zh-CN,zh;q=0.8

