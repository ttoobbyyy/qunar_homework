package com.toby;

import com.toby.entity.Cmd;
import com.toby.sevice.CmdExecutor;
import com.toby.sevice.Parser;
import com.toby.sevice.impl.CmdExecutorImpl;
import com.toby.sevice.impl.ParserImpl;

import java.util.List;
import java.util.Scanner;

/**
 * @author xiaoxl
 * @date 2022/6/16 16:54
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("# Welcome mini Linux shell!!!");
        System.out.println("# We support wc, grep, cat commands");
        System.out.println("# Enter 'q' or 'exit' to exit shell");

        while (true){
            System.out.print("(^-^)>");
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.nextLine();
            if (cmd.length() == 0){
                continue;
            }
            if (cmd.equals("q") || cmd.equals("exit")){
                break;
            }
            if (!execute(cmd)){
                System.out.println("[Error] Failed to execute '"+cmd+"'");
            }
        }
        System.out.println("bye ~~~");
    }

    private static boolean execute(String cmd) {
        // 解析命令
        Parser parser = new ParserImpl();
        List<Cmd> cmds = parser.parse(cmd);
        // 执行命令
        CmdExecutor executor = new CmdExecutorImpl();
        boolean result = false;
        try {
            result = executor.executorCmds(cmds);
        }catch (Exception e){
            if (e.getMessage() != null)
                System.out.println("[Error] "+e.getMessage());
            return true;
        }
        return result;
    }
}
