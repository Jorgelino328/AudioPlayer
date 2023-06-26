package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.MainApplication;
import com.ufrn.audioplayer.dao.DiretoriosDAO;
import com.ufrn.audioplayer.dao.MusicasDAO;
import com.ufrn.audioplayer.dao.PlaylistsDAO;
import com.ufrn.audioplayer.dao.UsuariosDAO;
import com.ufrn.audioplayer.model.Musica;
import com.ufrn.audioplayer.model.Playlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VIPController extends MainController implements Initializable {

    private PlaylistsDAO bdPlaylist;
    private MusicasDAO bdMusica;
    private UsuariosDAO bdUsuario;



    @FXML
    private TextFlow playlistsList;
    @FXML
    private TextField playlist_name;
    @FXML
    private ComboBox<String> selectMusica;
    @FXML
    private ComboBox<String> selectPlaylist;


    private Playlist currentPlaylist;
    private int indexPlaylist = 0;

    /**
     * Funcão inicializadora do VIPController, além de repetir o que o initialize do MainController faz por meio de polimorfismo, também lê as playlists do usuário
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url,resourceBundle);
        bdPlaylist = PlaylistsDAO.getInstance();
        bdMusica = MusicasDAO.getInstance();
        bdUsuario = UsuariosDAO.getInstance();


        for (int i = 0; i < bdMusica.getListaMusicas().size();i++) {
            selectMusica.getItems().add(bdMusica.getListaMusicas().get(i).getId() + " - " + bdMusica.getListaMusicas().get(i).getFile().getName());
        }

        for (int i = 0; i < bdPlaylist.getPlaylists().size();i++){
            selectPlaylist.getItems().add(bdPlaylist.getPlaylists().get(i).getId() + " - " + bdPlaylist.getPlaylists().get(i).getNome());
            if(bdPlaylist.getPlaylists().get(i).getUsr_id() == bdUsuario.getUsuarioAtual().getId() ){
                Label label = new Label(i+". " +bdPlaylist.getPlaylists().get(i).getNome()+"\n");
                Button btn = new Button("Play");
                HBox hbox = new HBox();
                hbox.getChildren().addAll(label,btn);
                int finalI = i;
                btn.setOnAction(event -> playPlaylist(bdPlaylist.getPlaylists().get(finalI),indexPlaylist));
                label.setPrefWidth(playlistsList.getPrefWidth() - 47);
                label.centerShapeProperty();
                playlistsList.getChildren().add(hbox);
            }

        }

    }

    /**
     * Adiciona uma nova playlist
     * @param e
     */
    public void addMusicaPlaylist(ActionEvent e){
        bdPlaylist = PlaylistsDAO.getInstance();
        ArrayList<Musica> musicas = new ArrayList<Musica>();
        String[] musicasData = selectMusica.getValue().split(" - ");
        Musica m = bdMusica.getListaMusicas().get(Integer.parseInt(musicasData[0]));
        musicas.add(m);

        String[] PlaylistData = selectPlaylist.getValue().split(" - ");

        Playlist playlist = bdPlaylist.getPlaylists().get(Integer.parseInt(PlaylistData[0])-1);
        playlist.setMusicas_playlist(musicas);
        bdPlaylist.addMusica(playlist, m);

    }

    /**
     * Cadastra um novo usuário
     * @param e
     * @throws IOException
     */
    public void cadastraUsuario(ActionEvent e) throws IOException {
        Parent tableViewParent  = FXMLLoader.load(MainApplication.class.getResource("view/cadastro.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setTitle("AudioPlayer - Cadastrar Usuário");
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Cadastra uma nova playlist
     * @param e
     */
    public void novaPlaylist(ActionEvent e){
        bdPlaylist = PlaylistsDAO.getInstance();
        bdUsuario = UsuariosDAO.getInstance();
        ArrayList<Musica> musicas = new ArrayList<Musica>();
        Playlist playlist = new Playlist(bdUsuario.getUsuarioAtual().getId(),playlist_name.getText(),musicas);
        bdPlaylist.addPlaylist(playlist);
        bdPlaylist.saveFile(playlist);
        int index = bdPlaylist.getPlaylists().size() > 0 ? bdPlaylist.getPlaylists().size()-1 : 0;
        selectPlaylist.getItems().add(bdPlaylist.getPlaylists().get(index).getId() + " - " + bdPlaylist.getPlaylists().get(index).getNome());
        Label label = new Label(index+". " +bdPlaylist.getPlaylists().get(index).getNome()+"\n");
        Button btn = new Button("Play");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(label,btn);
        label.setPrefWidth(playlistsList.getPrefWidth());
        playlistsList.getChildren().add(hbox);
    }

    /**
     * Reseta a playlist caso acionada
     * @param m
     */
    @Override
    public void playMusica(Musica m){
        if(currentPlaylist != null){
            currentPlaylist = null;
        }
        super.playMusica(m);
    }

    /**
     * Toca apenas as músicas referentes à playlist ativa
     * @param p
     * @param indexPlaylist
     */
    public void playPlaylist(Playlist p,int indexPlaylist){
        if(currentPlaylist != null){
            playMusica(p.getMusicas_playlist().get(indexPlaylist));
        }else{
            indexPlaylist = 0;
            playMusica(p.getMusicas_playlist().get(indexPlaylist));
        }
    }

    /**
     * Volta para a música anterior a atual na fila da playlist
     */
    @Override
    public void previousMedia(){
        if(currentPlaylist != null) {
            if(currentPlaylist.getMusicas_playlist().size() < indexPlaylist - 1) {
                playPlaylist(currentPlaylist, indexPlaylist - 1);
                indexPlaylist--;
            }else{
                playPlaylist(currentPlaylist, 0);
                indexPlaylist = 0;
            }
        }else{
            super.previousMedia();
        }
    }

    /**
     * Avança até a próxima música na fila da playlist atual
     */
    @Override
    public void nextMedia() {
        if(currentPlaylist != null) {
            if(currentPlaylist.getMusicas_playlist().size() > indexPlaylist + 1) {
                playPlaylist(currentPlaylist, indexPlaylist + 1);
                indexPlaylist++;
            }else{
                playPlaylist(currentPlaylist, 0);
                indexPlaylist = 0;
            }
        }else{
            super.nextMedia();
        }
    }
}