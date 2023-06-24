package com.ufrn.audioplayer.dao;

import java.io.*;
import java.util.ArrayList;

import com.ufrn.audioplayer.model.Usuario;


public class UsuariosDAO {
    ArrayList<Usuario> listaUsuarios;
    private File directory;
    private File[] files;
    private static UsuariosDAO uDao;

    public UsuariosDAO() {
        listaUsuarios = new ArrayList<Usuario>();

        directory = new File("src/main/resources/com/ufrn/audioplayer/saves");
        files = directory.listFiles();
        if(files != null){
            for(File file : files){
                readFile(file);
            }
        }
    }

    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                String name = userData[0];
                String senha = userData[1];
                System.out.println("Name: " + name + ", Senha: " + senha);
            }

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }
    // Singleton
    public static UsuariosDAO getInstance() {
        if (uDao == null) {
            uDao = new UsuariosDAO();
        }
        return uDao;
    }

    public void addUsuario(Usuario usuario){
        listaUsuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario){
        listaUsuarios.remove(usuario);
    }

    public boolean validaUsuario(String usuario, String senha){
        for(int i=0; i<listaUsuarios.size();i++){
            if(listaUsuarios.get(i).getNome().equals(usuario) && listaUsuarios.get(i).getSenha().equals(senha)){
                return true;
            }
        }
        return false;
    }
}
