package com.toby;

import com.toby.service.ClientImpl;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author: xiaoxl
 * @Date: 2022/6/18
 */
public class Client {
    private static final Integer PORT = 6666;

    public static void main(String[] args) {
        ClientImpl client = new ClientImpl("127.0.0.1", PORT);
        new Thread(client).start();

        System.out.println("# Input the valid http url !");
        System.out.println("# Enter 'q' or 'exit' to exit the client!");
        System.out.print("(valid http url)>");
        Scanner scanner = new Scanner(System.in);
        while (true){
            String content = scanner.nextLine();
            if (content.equals("q") || content.equals("exit")){
                client.stop();
                break;
            }
            client.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }
}
