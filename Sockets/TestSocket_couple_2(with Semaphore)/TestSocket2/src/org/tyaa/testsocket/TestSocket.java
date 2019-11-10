package org.tyaa.testsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Демо-проект клиент-сервер (десктоп - десктоп) по WiFi (серверная сторона)
 * @author Юрий
 */
public class TestSocket {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    //Точка входа в приложение
    public static void main(String[] args) throws IOException
            , InterruptedException {
        //Создаем объект сервер-сокет для прослушивания порта 3000
        ServerSocket serverSocket = new ServerSocket(3000);
        //Создаем пул потоков выполнения на 10 потоков
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        //Создаем объект-семафор с разрешениями для 10 потоков выполнения
        Semaphore semaphore = new Semaphore(10);
        //Запускаем бесконечный цикл прослушивания порта
        while(true){
            //Выдаем разрешение одному потоку выполнения
            semaphore.acquire();
            //Ожидаем запроса к серверу
            Socket acceptSocket = serverSocket.accept();
            //Когда запрос получен, запускаем в отдельном потоке метод
            //для его обработки, передавая методу объект Socket
            fixedThreadPool.execute(()->{
                try(Socket _acceptSocket = acceptSocket){
                    serve(_acceptSocket);
                }
                catch(Exception ex){
                    System.out.println(ex);
                }
                //Независимо от того, удалось ли обработать запрос,
                //возвращаем разрешение семафору
                finally{
                    semaphore.release();
                }
            });
        }
    }
    //Метод обработки запроса к серверу, который нужно вызывать
    //в отдельном потоке выполнения
    private static void serve(final Socket _acceptSocket) throws IOException{
        //Переменная для получения строки-результата
        String resultLineString;
        //Создаем потоки ввода и вывода, подключенные к объекту Socket
        InputStream inputStream = _acceptSocket.getInputStream();
        OutputStream outputStream = _acceptSocket.getOutputStream();
        //Для упрощения ввода строковых данных от приложения-клиента
        //создаем высокоуровневую обертку потока ввода
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(inputStream));
        //Для упрощения вывода ответа приложению-клиенту
        //создаем высокоуровневую обертку потока вывода
        OutputStreamWriter outputStreamWriter =
                new OutputStreamWriter(outputStream, "utf8");
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter, true);
        //Читаем потоком ввода текстовые строки, ожидаемые от клиента
        while(true){
            resultLineString = bufferedReader.readLine();
                //Если на текущей итерации от клиента поступила строка
                if(resultLineString != null)
                {
                    //Формируем и отправляем строку-ответ клиенту
                    printWriter.println(resultLineString + " world!!!");
                    //Печатаем принятую от клиента строку в консоль
                    System.out.println(resultLineString);
                }
        }
    }
    
}
