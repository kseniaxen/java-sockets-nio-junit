/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.testsocketclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 *
 * @author student
 */
public class RecvThread extends Thread {
    
    public SocketChannel socketChannel = null;
    public boolean val = true;

    public RecvThread(String str, SocketChannel client) {
        super(str);
        socketChannel = client;
    }

    @Override
    public void run() {

        System.out.println("Inside receivemsg");
        int nBytes = 0;
        ByteBuffer buf = ByteBuffer.allocate(2);
        try {
            String totalResponseString = "";
            while (val) {
                //Читаем строку-ответ сервера
                while ((nBytes = socketChannel.read(buf)) > 0) {
                    
                    buf.flip();
                    Charset charset = Charset.forName("us-ascii");
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer charBuffer = decoder.decode(buf);
                    String currentResponseString = charBuffer.toString();
                    System.out.println(currentResponseString);
                    buf.flip();
                    
                    if (!currentResponseString.equals("")) {
                            
                            totalResponseString += currentResponseString;
                            if (totalResponseString.contains("The end")) {
                                
                                break;
                            }
                        }
                }
            }
        } catch (IOException e) {
            
            System.out.println("Сервер недоступен");
            System.exit(0);
        }
    }
}
