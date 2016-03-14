package sample;

import LyricFiles.LyricInfo;
import LyricFiles.ViewLyricsSearcher;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.sun.javafx.scene.control.Logging;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.logging.Log;
import org.apache.http.client.HttpClient;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Controller implements Initializable{

    public TableView table;
    public TextField Artist;
    public TextField title;
    public Button search;
    public Button next;
    public Button button_no;
    public AnchorPane anchor;
    public int PageNo=0;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    Stage primaryStage;
    public Button prev;
    String response="";
    LyricsInfo lyricsInfo;
    public ProgressBar progressbar;

    ObservableList<LyricsInfo> data= FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        progressbar.setVisible(false);
        prev.setVisible(false);

//        TableColumn link = new TableColumn("Link");
//        link.setMinWidth(100);
//        link.setCellValueFactory(
//                new PropertyValueFactory<LyricsInfo, String>("link"));

        TableColumn Artist = new TableColumn("Artist");
        Artist.setMinWidth(100);
        Artist.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("artist"));

        TableColumn title = new TableColumn("Title");
        title.setMinWidth(250);
        title.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("title"));

        TableColumn album = new TableColumn("Album");
        album.setMinWidth(130);
        album.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("album"));

        TableColumn uploader = new TableColumn("Uploader");
        uploader.setMinWidth(100);
        uploader.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("uploader"));

        TableColumn rate = new TableColumn("Rating");
        rate.setMinWidth(100);
        rate.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("rate"));

        TableColumn ratecount = new TableColumn("Voted by");
        ratecount.setMinWidth(100);
        ratecount.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("ratecount"));

        TableColumn downloads = new TableColumn("Downloads");
        downloads.setMinWidth(100);
        downloads.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("downloads"));

        TableColumn timelength = new TableColumn("Timelength");
        timelength.setMinWidth(100);
        timelength.setCellValueFactory(
                new PropertyValueFactory<LyricsInfo, String>("timelength"));

        table.setRowFactory(new Callback<TableView, TableRow>() {
            @Override
            public TableRow call(TableView param) {
                final TableRow<LyricsInfo> row = new TableRow<>();
                row.hoverProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        lyricsInfo=row.getItem();
                        if (row.isHover() && lyricsInfo != null) {
                            Tooltip tooltip = new Tooltip("Artist : "+lyricsInfo.getArtist()+"\nTitle : "+lyricsInfo.getTitle()+"\nAlbum: "+lyricsInfo.getAlbum()+"\nUploader: "+lyricsInfo.getUploader()+"\nRating: "+lyricsInfo.getRate()+"\nDownloads: "+lyricsInfo.getDownloads());
                            tooltip.setFont(new Font("Arial", 14));
                            tooltip.setContentDisplay(ContentDisplay.CENTER);
                            row.setTooltip(tooltip);
//                            System.out.println("row is hovered");
                        } else {
//                            System.out.println("row isnt hovered");
                        }
                    }
                });
                row.setOnMousePressed(event -> {
                    if (event.isSecondaryButtonDown()) {
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem item1=new MenuItem("Show Lyrics");
                        item1.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                progressbar.setVisible(true);
                                Thread t=new Thread(() -> {
                                    response="";
                                    DownloadLyrics();
//                                        SaveLyrics();
                                    Platform.runLater(() -> {
                                        progressbar.setVisible(false);
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle(lyricsInfo.getArtist());
                                        alert.setHeaderText(lyricsInfo.getTitle());
                                        alert.setContentText(null);
                                        TextArea textArea = new TextArea(response);
                                        textArea.setEditable(false);
                                        textArea.setWrapText(true);
                                        textArea.setMaxWidth(Double.MAX_VALUE);
                                        textArea.setMaxHeight(Double.MAX_VALUE);
                                        GridPane.setVgrow(textArea, Priority.ALWAYS);
                                        GridPane.setHgrow(textArea, Priority.ALWAYS);

                                        GridPane expContent = new GridPane();
                                        expContent.setMaxWidth(Double.MAX_VALUE);
                                        expContent.add(textArea, 0, 1);

                                        alert.getDialogPane().setExpandableContent(expContent);
                                        alert.getDialogPane().setExpanded(true);
                                        alert.showAndWait();
                                    });
                                });
                                t.start();
                            }
                        });
                        MenuItem item2 = new MenuItem("Download lyrics");
                        item2.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                progressbar.setVisible(true);
                                Thread t=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response="";
                                        DownloadLyrics();
                                        SaveLyrics(lyricsInfo.getTitle());
                                    }
                                });
                                t.start();

                            }
                        });
                        MenuItem item3 = new MenuItem("Remove");
                        item3.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
//                                System.out.println("item2 clicked");
                                table.getItems().remove(lyricsInfo);
                                table.refresh();
                            }
                        });

                        contextMenu.getItems().addAll(item1, item2 ,item3);
                        row.setContextMenu(contextMenu);
//                            System.out.println("right clicked");

                    }
                });
                return row;
            }
        });

        table.getColumns().addAll(Artist, title, album, uploader, rate, ratecount, downloads, timelength);

        search.setOnMouseClicked(event -> {
            PageNo=0;
            prev.setVisible(false);
            button_no.setText(String.valueOf(PageNo+1));
            data.clear();
            try {
                getlyrics();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        next.setOnMouseClicked(event -> {
            PageNo++;
            prev.setVisible(true);
            button_no.setText(String.valueOf(PageNo + 1));
            data.clear();
            try {
                getlyrics();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        prev.setOnMouseClicked(event -> {
            if (PageNo >= 1) {
                PageNo--;
                if (PageNo>=1){
                    prev.setVisible(true);
                }else{
                    prev.setVisible(false);
                }
                button_no.setText(String.valueOf(PageNo + 1));
                data.clear();
                try {
                    getlyrics();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
//        table.refresh();

//        table.setOnMousePressed(event -> {
//            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
//                LyricsInfo info= (LyricsInfo) table.getSelectionModel().getSelectedItem();
//                System.out.println(info.getLink());
//            }
//        });



    }

    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex);
//            Logger.getLogger(JavaFX_Text.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getlyrics() throws InterruptedException {

        progressbar.setVisible(true);
        Thread t=new Thread(() -> {
            try {
                String artist="";
                String Title="";
                if (Artist.getText()!=null){
                    artist=Artist.getText();
                }if (title.getText()!=null){
                    Title=title.getText();
                }
                Iterator e = ViewLyricsSearcher.search(artist, Title, PageNo).getLyricsInfo().iterator();

                while (e.hasNext()) {
                    LyricInfo aresult = (LyricInfo) e.next();
                    LyricsInfo info = new LyricsInfo(aresult.getLyricURL(), aresult.getMusicArtist(), aresult.getMusicTitle(), aresult.getMusicAlbum(), aresult.getLyricUploader(), aresult.getLyricRate(), aresult.getLyricRatesCount(), aresult.getLyricDownloadsCount(), aresult.getMusicLenght());
                    data.add(info);
                }

            } catch (NoSuchAlgorithmException | SAXException | ParserConfigurationException | IOException var3 ) {
                var3.printStackTrace();
            }
            progressbar.setVisible(false);
        });
        t.start();
        table.setItems(data);
    }

    public void DownloadLyrics(){
        URL obj = null;
        try {
            obj = new URL(lyricsInfo.getLink());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional default is GET
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response+=inputLine+"\n";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SaveLyrics(String lyrictitle){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressbar.setVisible(false);
                FileChooser fileChooser = new FileChooser();
                //Set extension filter
                fileChooser.setTitle("Save file as");
                fileChooser.setInitialFileName(lyrictitle+".lrc");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(getPrimaryStage());

                if(file != null){
                    SaveFile(response, file);
                }
            }
        });

    }
}
