package com.ufrn.audioplayer.controller;

import com.ufrn.audioplayer.MainApplication;
import com.ufrn.audioplayer.dao.UsuariosDAO;
import com.ufrn.audioplayer.model.Usuario;
import com.ufrn.audioplayer.model.UsuarioVIP;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {

    private UsuariosDAO bdUsuarios;

    @FXML
    private TextField usuario_input;

    @FXML
    private PasswordField senha_input;

    @FXML
    private CheckBox isVIP;


    @FXML
    private Button cadastrar_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Cadastra um novo usuário no sistema
     * @param e
     * @throws IOException
     */
    public void cadastrar_btn_action(ActionEvent e) throws IOException {
        bdUsuarios = UsuariosDAO.getInstance();
        if(isVIP.isSelected()) {
            UsuarioVIP uVIP = new UsuarioVIP(usuario_input.getText(), senha_input.getText());
            bdUsuarios.addUsuario(uVIP);
            bdUsuarios.saveFile(uVIP);
        }else{
            Usuario u = new Usuario(usuario_input.getText(), senha_input.getText());
            bdUsuarios.addUsuario(u);
            bdUsuarios.saveFile(u);
        }
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
    }
}

