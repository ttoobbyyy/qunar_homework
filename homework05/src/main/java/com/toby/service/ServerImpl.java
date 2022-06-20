package com.toby.service;

import com.toby.entity.CountResult;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: xiaoxl
 * @Date: 2022/6/19
 */
public class ServerImpl implements Runnable{
    /**
     * 多路复用器
     */
    private Selector selector;
    /**
     * Server端的通道，之前没有想过用法竟然是当做selector的一个属性
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * 标识运行状态
     */
    private volatile boolean stop;

    public ServerImpl(int port){
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("# the server is start on port "+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // foreach handle ready event key
                for (SelectionKey key : selectionKeys){
                    try {
                        handleKey(key);
                    }catch (IOException e){
                        System.out.println("\n[Error] handle key");
                        key.cancel();
                        if (key.channel() != null){
                            key.channel().close();
                        }
                    }
                }
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selector != null){
            try {
                selector.close();
                System.out.println("\nbye ~~~");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * handle the event key
     * @param key
     * @throws IOException
     */
    private void handleKey(SelectionKey key) throws IOException{
        if (key.isValid()){
            // accept event
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel channel = ssc.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()){
                SocketChannel socketChannel = ((SocketChannel) key.channel());
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0){
                    Buffer flip = byteBuffer.flip();
                    byte[] bytes =  new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String url = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("\nreceive the '"+url+"' request from "+socketChannel.getRemoteAddress());
                    CountResult countResult = handleURL(url);
                    if (countResult != null)
                        doWrite(socketChannel, countResult.toString());
                    else
                        doWrite(socketChannel, "parse '"+url+"' error, please input the valid http request!");
                }else if (readBytes < 0){
                    // client has closed
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    /**
     * write response to client
     */
    private void doWrite(SocketChannel socketChannel, String response) throws IOException {
        if(response !=null && response.trim().length() >0){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
        }
    }

    /**
     * send http url and analyse the sort of character
     */
    private CountResult handleURL(String url) {
        CountResult countResult = new CountResult(0,0,0);
        try {
            // parse url
            URL parseURL = new URL(url);
            byte[] request = parseURL(url, parseURL);
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(parseURL.getHost(), 80));
            socketChannel.configureBlocking(false);

            while (!socketChannel.finishConnect()) {
                Thread.sleep(10);
            }
            socketChannel.write(ByteBuffer.wrap(request));

            int read = 0;
            boolean readed = false;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while ((read = socketChannel.read(buffer)) != -1){
                if (read == 0 && readed){
                    break;
                }else if (read == 0){
                    continue;
                }
                buffer.flip();
                sortCharacter(StandardCharsets.UTF_8.decode(buffer).array(), countResult);
                buffer.clear();
                readed = true;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("\n[Error]"+e.getMessage());
            return null;
        }
        return countResult;
    }

    private void sortCharacter(char[] chars, CountResult countResult) {
        int chinese = 0;
        int english = 0;
        int punctuation = 0;
        for (char c : chars){
            if (isEnglish(c)){
                english++;
            }else if (isChinese(c)){
                chinese++;
            }else if (isPunctuation(c)){
                punctuation++;
            }
        }
        countResult.setChineseCharacters(countResult.getChineseCharacters()+chinese);
        countResult.setEnglishCharacters(countResult.getEnglishCharacters()+english);
        countResult.setPunctuationCharacters(countResult.getPunctuationCharacters()+punctuation);
    }

    private boolean isPunctuation(char c) {
        // Pattern p = Pattern.compile("[\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】|\\!|\\,|\\.|\\(|\\)|\\<|\\>|\"|\\?|\\:|\\;|\\[|\\]|\\{|\\}|\\/]");
        Pattern p = Pattern.compile("\\pP");
        Matcher m = p.matcher(String.valueOf(c));
        return m.find();
    }

    private boolean isChinese(char c) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(String.valueOf(c));
        return m.find();
    }

    private boolean isEnglish(char c) {
        return (c >= 97 && c <= 122) || (c >= 65 && c <= 90);
    }

    /**
     * only Get request supported
     */
    private byte[] parseURL(String url, URL parseURL) {
        StringBuilder temp = new StringBuilder();
        assert parseURL != null;
        temp.append("GET ").append(url).append(" HTTP/1.1\r\n")
                .append("Host: ").append(parseURL.getHost()).append("\r\n")
                .append("Connection: keep-alive\r\n")
                .append("Cache-Control: max-age=0\r\n")
                .append("User-Agent: Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.47 Safari/536.11\r\n")
                .append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n")
                .append("\r\n");
        return temp.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * stop the server
     */
    public void stop(){
        stop = true;
    }
}
