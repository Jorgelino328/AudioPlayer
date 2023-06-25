package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.MainApplication;
import com.ufrn.audioplayer.model.UsuarioVIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.ufrn.audioplayer.dao.UsuariosDAO;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private UsuariosDAO bdUsuarios;

    @FXML
    private TextField usuario_input;

    @FXML
    private PasswordField senha_input;


    @FXML
    private Button entrar_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void entrar_btn_action(ActionEvent e) throws IOException {
        bdUsuarios = UsuariosDAO.getInstance();
        if(bdUsuarios.validaUsuario(usuario_input.getText(), senha_input.getText())){
            if(bdUsuarios.getUsuarioAtual() instanceof UsuarioVIP) {
                Parent tableViewParent = FXMLLoader.load(MainApplication.class.getResource("view/vip.fxml"));
                Scene tableViewScene = new Scene(tableViewParent);
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setTitle("AudioPlayer");
                window.setScene(tableViewScene);
                window.show();
            }else {
                Parent tableViewParent = FXMLLoader.load(MainApplication.class.getResource("view/main.fxml"));
                Scene tableViewScene = new Scene(tableViewParent);
                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
                window.setTitle("AudioPlayer");
                window.setScene(tableViewScene);
                window.show();
            }

        }else{
            System.out.println("ERRO NO LOGIN");
        }
    }

}
