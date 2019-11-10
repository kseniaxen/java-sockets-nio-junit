package org.itstep.mariupol.adce440datafilesgraph.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.itstep.mariupol.adce440datafilesgraph.MainApp;
import org.itstep.mariupol.adce440datafilesgraph.model.Channel;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class GraphOverviewController implements Initializable {
    
    @FXML
    private GridPane gridpane;
    
    private GridPane gridpanemenu;
    
    @FXML
    private LineChart<Number, Number> mChannelLineChart;
    
    private BorderPane mRootBorderPane;
    
    private ArrayList<Channel> mChannelsArrayList;
    private ArrayList<Series> mSeriesList;
    private ArrayList<Button> buttons;
    
    private MainApp mMainApp;
    
    private Stage mPrimaryStage;
    
    public void setPrimaryStage(Stage mPrimaryStage){
        this.mPrimaryStage=mPrimaryStage;
    }
    
    private ArrayList<String> pathFilesArrayList;
    
    public void setPathFilesArrayList(ArrayList<String> pathFilesArrayList){
        this.pathFilesArrayList=pathFilesArrayList;
    }
    
    public LineChart getLineChart(){
        return mChannelLineChart;
    }
    
    public GraphOverviewController() {
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        mSeriesList = new ArrayList<>();
        buttons = new ArrayList<>();
    }
    
    public void setMainApp(MainApp mainApp) {
        mMainApp = mainApp;

        mRootBorderPane = mMainApp.getRootBorderPane();
    }
    
    public void getMas()
    {
        String test;
        for (int i = 0; i < mChannelsArrayList.size(); i++) {
            //gridpane.add(new Label(""+(i+1)), 0, i+1);
            String channelNumber =
                    "" + mChannelsArrayList.get(i).getChannelNumber();
            gridpane.add(
                    new Label(channelNumber)
                , 0
                , i+1
            );
            CheckBox chk = new CheckBox();
            //chk.setId("chb"+(i+1));
            chk.setId("chb" + channelNumber);
            chk.setOnAction(e->actionChangeCheckBox(e));
            gridpane.add(chk, 1, i+1);
        }
    }
    
    public void addButton()
    {
        Button button = new Button("file_"+(pathFilesArrayList.size()));
        button.setId(button.getText());
        button.setOnAction(e->actionChangeFile(e));
        gridpanemenu.add(button, pathFilesArrayList.size(), 1);
        buttons.add(button);
    }
    
    public void setGridpanemenu(GridPane gridpanemenu){
        this.gridpanemenu=gridpanemenu;
    }
    
    boolean flag = true;
    
    public void showGraph(String filePathSring) {
        
        if(!flag)
        {
            mChannelsArrayList.clear();
            mSeriesList.clear();
            gridpane.getChildren().clear();
            mChannelLineChart.getData().clear();
        }
        flag=false;
        mChannelsArrayList = MainApp.getChannelsData(filePathSring);
        if(buttons.size()<pathFilesArrayList.size())
        {
            addButton();
        }
        getMas();
        int currentSeriesIdx = 0;
        for (Channel channel : mChannelsArrayList) {
            mSeriesList.add(new XYChart.Series());
            Series currentSeries = mSeriesList.get(currentSeriesIdx);
            //Integer currentSeriesNameInteger = currentSeriesIdx + 1;
            Integer currentSeriesNameInteger = channel.getChannelNumber();
            currentSeries.setName(currentSeriesNameInteger.toString());
            ObservableList<XYChart.Data> channelObservableList =
                    FXCollections.observableArrayList();
            for(int i_channelDataItem = 0; i_channelDataItem < channel.size() - 1; i_channelDataItem++) {
                channelObservableList.add(new XYChart.Data(i_channelDataItem, channel.getDataItem(i_channelDataItem)));
            }
            currentSeries.setData(channelObservableList);
            mChannelLineChart.getData().add(currentSeries);
            currentSeries.getNode().setVisible(false);
            currentSeriesIdx++;
        }
    }
    
    public void actionChangeCheckBox(ActionEvent actionEvent) {
        CheckBox checkBox = (CheckBox) actionEvent.getSource();
        String checkBoxIdx = checkBox.getId().replace("chb", "");
        Series currentSeries = null;
        for (Series series : mChannelLineChart.getData()) {
            if (series.getName().equals(checkBoxIdx)) {
                currentSeries = series;
            }
        }
        //mChannelLineChart.getData().remove(currentSeries);
        Node currentSeriesNode = currentSeries.getNode();
        if (currentSeriesNode.isVisible()) {
            currentSeriesNode.setVisible(false);
        } else {
            currentSeriesNode.setVisible(true);
        }
    }
    
    public void actionChangeFile(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        
        String buttonIdx = button.getId().replace("file_", "");
        int count = 0;
        for (String path : pathFilesArrayList) {
            if (buttonIdx.equals((count + 1)+"")) {
                showGraph(path);
            }
            count++;
        }
    }
}
