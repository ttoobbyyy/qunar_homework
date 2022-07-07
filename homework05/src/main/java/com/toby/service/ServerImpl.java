package com.toby.service;

import com.google.common.io.ByteStreams;
import com.toby.entity.CountResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
     * 标识运行状态
     */
    private volatile boolean stop;

    public ServerImpl(int port){
        try {
            selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
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
        InputStream inputStream = null;
        try {
            // parse url
            URL parseURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) parseURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                String contents = new String(ByteStreams.toByteArray(inputStream));
                sortCharacter(contents.toCharArray(), countResult);
            }
        } catch (IOException e) {
            System.out.println("\n[Error]"+e.getMessage());
            return null;
        }finally {
            Optional.ofNullable(inputStream).ifPresent(stream->{
                try {
                    stream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
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
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * stop the server
     */
    public void stop(){
        stop = true;
    }
}
