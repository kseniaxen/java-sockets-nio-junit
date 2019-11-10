/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testsocketclient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Юрий
 */
public class TestSocketClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Переменная сокета - для отправки информации серверу
        Socket socket = null;
        //Переменные потоков ввода-вывода для чтения/записи в сеть/из сети и
        //для буферизации
        InputStream inputStream = null;
        OutputStream outputStream = null;        
        OutputStreamWriter outputStreamWriter = null;        
        BufferedWriter bufferedWriter = null;
        //Создаем объект сокет для запросов на localhost, порт 3000
        try {
            socket = new Socket("localhost", 3000);
        } catch (IOException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем поток вывода
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем поток записи символов на основе потока вывода
        try {
            outputStreamWriter = new OutputStreamWriter(outputStream, "utf8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Инициализируем буфферизированный поток вывода        
        bufferedWriter = new BufferedWriter(outputStreamWriter);
        //Отправляем через поток записи строку серверу
        try {
            outputStreamWriter.write("hello ");
            outputStreamWriter.write("world ");
            outputStreamWriter.write("!\n");
        
        //Очищаем буфер, чтобы строка отправилась
        bufferedWriter.flush();
        outputStreamWriter.write("end");
        bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Закоываем поток
        try {
            outputStreamWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(TestSocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
