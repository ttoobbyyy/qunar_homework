package com.toby;


import com.toby.service.ServerImpl;

import java.util.Scanner;

/**
 * @Author: xiaoxl
 * @Date: 2022/6/18
 */
public class Server {
    private static final Integer PORT = 6666;

    public static void main(String[] args) {
        // core logic
        ServerImpl server = new ServerImpl(PORT);
        new Thread(server).start();

        System.out.println("# Welcome URL agent Server !");
        System.out.println("# Enter 'q' or 'exit' to exit!");
        Scanner scanner = new Scanner(System.in);
        while (true){
            String str = scanner.nextLine();
            System.out.print("('q' or 'exit')>");
            if (str.equals("q") || str.equals("exit")){
                server.stop();
                break;
            }
        }
    }
}
