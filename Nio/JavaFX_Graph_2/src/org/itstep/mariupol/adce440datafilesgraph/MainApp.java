/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.itstep.mariupol.adce440datafilesgraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.itstep.mariupol.adce440datafilesgraph.model.Channel;
import org.itstep.mariupol.adce440datafilesgraph.view.GraphOverviewController;
import org.itstep.mariupol.adce440datafilesgraph.view.RootLayoutController;

/**
 *
 * @author Администратор
 */
public class MainApp extends Application {

    private GridPane gridpanemenu;
    //Переменная для сцены главного окна
    private Stage mPrimaryStage;
    //Переменная для представления (графичекого контейнера) главного окна
    private BorderPane mRootLayout;
    //Переменная для контроллера главного окна
    private RootLayoutController mRootLayoutController;
    
    //Объект для хранения коллекции объектов "канал"
    private static ArrayList<Channel> mChannelsArrayList = new ArrayList<>();
    //Переменная для хранения пути открываемого файла данных
    private static String mFilePathString;

    
    //Метод центрального контроллера MainApp,
    //вызываемый, когда приложение запускается
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Инициализируем переменную сцены главного окна
        mPrimaryStage = primaryStage;
        //Устанавливаем заголовок главного окна
        mPrimaryStage.setTitle("AdcE440DataFilesGraph");
        //Вызываем свой метод настройки и запуска главного окна
        initRootLayout();
        //Вызываем свой метод настройки и подключения в главное окно
        //представления области контента (и его контроллера)
        showGraphOverview();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public MainApp() {
    }

    /**
     * Returns the data as an observable list of Channels
     *
     * @param filePathString
     * @return
     */
    public static ArrayList<Channel> getChannelsData(String filePathString) {
        // Add channels data
        if(filePathString != null) {
            mFilePathString = filePathString;
        } else {
            mFilePathString = "D:/Temp/10f";
        }
        File fileParams = new File(mFilePathString + ".prm");
        if (fileParams.exists()) {
            JavaParamsReader paramsReader = new JavaParamsReader();
            mChannelsArrayList = paramsReader.getParams(mFilePathString);
            DataReader dataReader = new DataReader();
            dataReader.getData(mFilePathString, mChannelsArrayList);
        }
        
        return mChannelsArrayList;
    }
    
    public void setFilePathString(String filePathString) {
        mFilePathString = filePathString;
    }

    public Stage getPrimaryStage() {
        return mPrimaryStage;
    }
    
    public BorderPane getRootBorderPane() {
        return mRootLayout;
    }

    //метод настройки и запуска главного окна
    private void initRootLayout() { 
        try {
            // Загружаем разметку для главного окна
            //и устанавливаем ее в представление
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            mRootLayout = (BorderPane) loader.load();
            //Инициализируем переменную контроллера главного окна
            mRootLayoutController = loader.getController();
            //Создаем эпизод на базе представления главного окна,
            //устанавливаем его в сцену главного окна и отображаем сцену
            Scene scene = new Scene(mRootLayout);
            scene.getStylesheets().add((getClass().getResource("view/DarkTheme.css")).toExternalForm());
            mPrimaryStage.setScene(scene);
            this.gridpanemenu=mRootLayoutController.getGridpanemenu();
            mPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void showGraphOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GraphOverview.fxml"));
            AnchorPane graphOverview = (AnchorPane) loader.load();
            // Set person overview into the center of root layout.
            mRootLayout.setCenter(graphOverview);
            // Give the controller access to the main app.
            GraphOverviewController graphOverviewController = loader.getController(); 
            //Передача ссылки главного окна в класс RootLayoutController
            graphOverviewController.setGridpanemenu(this.gridpanemenu);
            graphOverviewController.setPrimaryStage(this.mPrimaryStage);
            graphOverviewController.setMainApp(this);    
            //Передача ссылки graphOverviewController в класс RootLayoutController
            mRootLayoutController.setGraphOverviewController(graphOverviewController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
