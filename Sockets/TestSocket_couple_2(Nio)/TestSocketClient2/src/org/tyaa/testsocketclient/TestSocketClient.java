/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tyaa.testsocketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 *
 * @author Юрий
 */
public class TestSocketClient {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws InterruptedException {
        //Переменная для клиентского сокета
        SocketChannel socketChannel = null;
        //Переменная для строки-ответа сервера
        String responseString = null;

        try {
            //Создаем клиентский сокет с указанием IP-адреса,
            // по которому будет отправляться запрос, и номера порта,
            // на котором слушает сервер
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 3000));
            socketChannel.configureBlocking(false);
            receiveMessage(socketChannel);
        } catch (IOException ex) {
            System.out.println("Error1");
        }
        Scanner sc = new Scanner(System.in);
        
        /* NIO */
        ByteBuffer responseByteBuffer = ByteBuffer.allocate(2);
        byte[] responseArray = new byte[responseByteBuffer.limit()];
        
        //ByteBuffer requestByteBuffer = ByteBuffer.allocate(2);
        /**/
        
        try {
            while (true) {
                
                System.out.print("> ");
                String commString = sc.next();
                //Отправляем серверу строку-запрос
                socketChannel.write(ByteBuffer.wrap(commString.getBytes()));
                Thread.sleep(1000);
                if (commString.equals("stop-domain")) {
                    System.exit(0);
                }
            }
        } catch (IOException ex) {
            System.out.println("Сервер недоступен");
        }
    }
    
    public static void receiveMessage(SocketChannel _socketChannel) {
        
        RecvThread rt = new RecvThread("Receive THread", _socketChannel);
        rt.start();
    }
}
