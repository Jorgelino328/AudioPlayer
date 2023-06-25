package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.MainApplication;
import com.ufrn.audioplayer.dao.PlaylistsDAO;
import com.ufrn.audioplayer.dao.UsuariosDAO;
import com.ufrn.audioplayer.model.Playlist;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class VIPController extends MainController implements Initializable {

    private PlaylistsDAO bdPlaylist;
    @FXML
    private TextField playlist_name;

    public void cadastraUsuario(ActionEvent e) throws IOException {
        Parent tableViewParent  = FXMLLoader.load(MainApplication.class.getResource("view/cadastro.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
        window.setTitle("AudioPlayer - Cadastrar Usuário");
        window.setScene(tableViewScene);
        window.show();
    }

    public void novaPlaylist(ActionEvent e){
        bdPlaylist = PlaylistsDAO.getInstance();
        ArrayList<Integer> musicas = new ArrayList<Integer>();
        musicas.add(1);
        musicas.add(2);
        Playlist playlist = new Playlist(1,playlist_name.getText(),musicas);
        bdPlaylist.addPlaylist(playlist);
        bdPlaylist.saveFile(playlist);
    }

}