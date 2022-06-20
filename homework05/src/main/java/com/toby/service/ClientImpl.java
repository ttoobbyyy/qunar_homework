package com.toby.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * @Author: xiaoxl
 * @Date: 2022/6/19
 */
public class ClientImpl implements Runnable{
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public ClientImpl(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
            System.out.println("\n# client connected !");
        }catch (IOException e){
            e.printStackTrace();
        }
        while(!stop){
            try{
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys){
                    try{
                        handleKey(key);
                    }catch (Exception e){
                        System.out.println("\n[Error] handle key!");
                        if(key != null){
                            key.cancel();
                            if(key.channel() !=null)
                                key.channel().close();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if(key.isValid()){
            SocketChannel sc = (SocketChannel)key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                }else{
                    System.out.println("connect error!");
                    System.exit(1);//连接失败，进程退出
                }
            }
            if(key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("\nthe response is :" + body);
                } else if (readBytes < 0) {
                    // the server is closed
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    public void write(byte[] content){
        ByteBuffer byteBuffer = ByteBuffer.allocate(content.length);
        byteBuffer.put(content);
        byteBuffer.flip();
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            System.out.println("\nrequest send failed: "+ new String(content));
            e.printStackTrace();
        }
    }

    public void stop(){
        System.out.println("\nbye ~~~");
        this.stop = true;
    }
}
