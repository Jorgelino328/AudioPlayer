package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.ufrn.audioplayer.dao.UsuariosDAO;



import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static UsuariosDAO uDao = new UsuariosDAO();

    @FXML
    private TextField usuario_input;

    @FXML
    private PasswordField senha_input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private Button entrar_btn;
    public void entrar_btn_action(ActionEvent e){
        if(uDao.validaUsuario(usuario_input.getText(), senha_input.getText())){
            System.out.println("LOGIN BEM SUCEDIDO");
        }
    }

}
