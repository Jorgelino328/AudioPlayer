package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.MainApplication;
import com.ufrn.audioplayer.dao.DiretoriosDAO;
import com.ufrn.audioplayer.dao.MusicasDAO;
import com.ufrn.audioplayer.dao.UsuariosDAO;
import com.ufrn.audioplayer.model.Musica;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainController implements Initializable {

    private DiretoriosDAO bdDiretorios;
    private MusicasDAO bdMusicas;
    private UsuariosDAO bdUsuario;
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

    @FXML
    private TextFlow musicasList;

    @FXML
    private Label userLabel;


    private Media media;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;

    private int songNumber;
    private double[] speeds = {0.25,0.50,0.75,1,1.25,1.5,1.75,2};

    private Timer timer;
    private TimerTask task;
    boolean running;

    /**
     * Função Inicializadora do MainController, lê os arquivos de áudio armazenados necessários para rodar a aplicação
     * @param url
     * @param resourceBundle
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bdUsuario = UsuariosDAO.getInstance();
        userLabel.setText(bdUsuario.getUsuarioAtual().getNome());
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
            for (int i = 0; i < bdMusicas.getListaMusicas().size();i++){
                Label label = new Label(i+". " +bdMusicas.getListaMusicas().get(i).getFile().getName()+"\n");
                Button btn = new Button("Play");
                HBox hbox = new HBox();
                hbox.getChildren().addAll(label,btn);
                int finalI = i;
                btn.setOnAction(event -> playMusica(bdMusicas.getListaMusicas().get(finalI)));
                label.setPrefWidth(musicasList.getPrefWidth() -47);
                label.centerShapeProperty();
                musicasList.getChildren().add(hbox);
            }
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

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    /**
     * Função que toca uma música específica m
     * @param m
     */
    public void playMusica(Musica m) {
        pauseMedia();

        if(running)
            cancelTimer();

        media = new Media(m.getFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(m.getFile().getName());

        playMedia();
    }

    /**
     * Salva um novo diretório e
     * @param e
     */
    public void novoDiretorio(ActionEvent e){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog((Stage)((Node) e.getSource()).getScene().getWindow());
        bdDiretorios = DiretoriosDAO.getInstance();
        bdDiretorios.addDiretorio(file);
        bdDiretorios.saveFile(file);
    }

    /**
     * Salva um novo arquivo de áudio e
     * @param e
     */
    public void novoArquivo(ActionEvent e){
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog((Stage)((Node) e.getSource()).getScene().getWindow());
        bdMusicas = MusicasDAO.getInstance();
        Musica m = new Musica(file);
        bdMusicas.addMusica(m);
        bdMusicas.saveFile(m);
    }

    /**
     * Toca a música na fila atual
     */
    public void playMedia(){
        beginTimer();
        changeSpeed(null);
        mediaPlayer.play();
    }

    /**
     * Pausa a música atual
     */
    public void pauseMedia(){
        if(running)
            cancelTimer();
        mediaPlayer.pause();
    }

    /**
     * Reseta a música atual para o início da mesma
     */
    public void resetMedia(){
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    /**
     * Volta para a música anterior a atual na fila
     */
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

    /**
     * Avança para a próxima música da fila
     */
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

    /**
     * Começa um timer para monitorar o tempo de duração da música
     */
    public void beginTimer(){
        timer = new Timer();
        task = new TimerTask() {

            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {
                    cancelTimer();
                }

            }
        };
        timer.scheduleAtFixedRate(task,0,1000);
    }

    /**
     * Cancela o timer
     */
    public void cancelTimer(){
        running = false;
        timer.cancel();
    }

    /**
     * Sai da aplicação
     * @param e
     * @throws IOException
     */
    public void logout(ActionEvent e) throws IOException {
        Parent tableViewParent = FXMLLoader.load(MainApplication.class.getResource("view/login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setTitle("AudioPlayer");
        window.setScene(tableViewScene);
        window.show();
    }
}