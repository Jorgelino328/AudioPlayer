package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.dao.DiretoriosDAO;
import com.ufrn.audioplayer.dao.MusicasDAO;
import com.ufrn.audioplayer.model.Musica;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainController implements Initializable {

    private DiretoriosDAO bdDiretorios;
    private MusicasDAO bdMusicas;

    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;

    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;

    @FXML
    private ComboBox<String> speedBox;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ProgressBar songProgressBar;

    private Media media;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;

    private int songNumber;
    private double[] speeds = {0.25,0.50,0.75,1,1.25,1.5,1.75,2};

    private Timer timer;
    private TimerTask task;
    private boolean running;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bdDiretorios = DiretoriosDAO.getInstance();
        bdMusicas = MusicasDAO.getInstance();
        for (int i=0;i<bdDiretorios.getListaDiretorios().size();i++) {
            directory = new File(bdDiretorios.getListaDiretorios().get(i).getPath());
            files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    Musica m = new Musica(file);
                    bdMusicas.addMusica(m);
                }
            }
        }

        try {
            media = new Media(bdMusicas.getListaMusicas().get(songNumber).getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(bdMusicas.getListaMusicas().get(songNumber).getFile().getName());

            for(int i = 0; i < speeds.length; i++){
                speedBox.getItems().add(speeds[i] +"x");
            }

            speedBox.setOnAction(this::changeSpeed);

            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                }
            });
        }catch (Exception e){
            System.out.println("Erro ao tentar abrir pasta de músicas: " + e);
        }
    }

    public void novoDiretorio(ActionEvent e){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog((Stage)((Node) e.getSource()).getScene().getWindow());
        bdDiretorios = DiretoriosDAO.getInstance();
        bdDiretorios.addDiretorio(file);
        bdDiretorios.saveFile(file);
    }

    public void novoArquivo(ActionEvent e){
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog((Stage)((Node) e.getSource()).getScene().getWindow());
        bdMusicas = MusicasDAO.getInstance();
        Musica m = new Musica(file);
        bdMusicas.addMusica(m);
        bdMusicas.saveFile(m);
    }
    public void playMedia(){
        beginTimer();
        changeSpeed(null);
        mediaPlayer.play();
    }
    public void pauseMedia(){
        cancelTimer();
        mediaPlayer.pause();
    }
    public void resetMedia(){
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }
    public void previousMedia(){
        if(songNumber > 0){
            songNumber--;
            pauseMedia();

            if(running)
                cancelTimer();

            media = new Media(bdMusicas.getListaMusicas().get(songNumber).getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(bdMusicas.getListaMusicas().get(songNumber).getFile().getName());

            playMedia();
        } else {
            songNumber = bdMusicas.getListaMusicas().size() - 1;
            pauseMedia();

            if(running)
                cancelTimer();

            media = new Media(bdMusicas.getListaMusicas().get(songNumber).getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(bdMusicas.getListaMusicas().get(songNumber).getFile().getName());

            playMedia();
        }
    }
    public void nextMedia(){
        if(songNumber < bdMusicas.getListaMusicas().size() - 1){
            songNumber++;
            pauseMedia();

            if(running)
                cancelTimer();

            media = new Media(bdMusicas.getListaMusicas().get(songNumber).getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(bdMusicas.getListaMusicas().get(songNumber).getFile().getName());

            playMedia();
        } else {
            songNumber = 0;
            pauseMedia();

            if(running)
                cancelTimer();

            media = new Media(bdMusicas.getListaMusicas().get(songNumber).getFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(bdMusicas.getListaMusicas().get(songNumber).getFile().getName());

            playMedia();
        }
    }
    public void changeSpeed(ActionEvent event){
        if(speedBox.getValue() == null){
            mediaPlayer.setRate(1);
        } else {
            speedBox.setPromptText(speedBox.getValue());
            mediaPlayer.setRate(Double.parseDouble(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)));
        }
    }

    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {

            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                //System.out.println(current/end);
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {
                    cancelTimer();
                }

            }
        };
        timer.scheduleAtFixedRate(task,0,1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }
}