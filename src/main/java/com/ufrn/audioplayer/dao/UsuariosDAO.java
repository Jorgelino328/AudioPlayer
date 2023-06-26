package com.ufrn.audioplayer.dao;

import java.io.*;
import java.util.ArrayList;
import com.ufrn.audioplayer.model.Usuario;
import com.ufrn.audioplayer.model.UsuarioVIP;

public class UsuariosDAO {
    ArrayList<Usuario> listaUsuarios;
    Usuario usuarioAtual;
    private File file = new File("src/main/resources/com/ufrn/audioplayer/saves/usuarios.txt");

    private static UsuariosDAO uDao;

    public UsuariosDAO() {
        listaUsuarios = new ArrayList<Usuario>();
        if(file != null){
            readFile(file);
        }
    }


    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }

    public void setUsuarioAtual(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
    }


    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(", ");
                String name = userData[0];
                String senha = userData[1];
                boolean isVIP = Boolean.parseBoolean(userData[2]);
                Usuario new_usr;
                if(isVIP){
                    new_usr = new UsuarioVIP(name, senha);
                }else{
                    new_usr = new Usuario(name, senha);
                }
                //System.out.println("Name: "+name+" Senha: "+senha+" VIP: "+isVIP);
                addUsuario(new_usr);
            }

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    public void saveFile(Usuario usuario){
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(usuario.getNome()+", "+usuario.getSenha()+", "+(usuario instanceof UsuarioVIP));
            bufferedWriter.close();
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
        usuario.setId(listaUsuarios.size());
    }

    public void removeUsuario(Usuario usuario){
        listaUsuarios.remove(usuario);
    }

    public String listarUsuarios() {
        System.out.println("Usuarios:");
        System.out.println("------------------------------------------------");
        String output = "";
        for (Usuario u : listaUsuarios) {
            output += "\tNome: " + u.getNome() +
                    "\n";
            output += "----------------------------------------------------------\n";
        }
        return output;
    }

    public boolean validaUsuario(String usuario, String senha){

        for(int i=0; i<listaUsuarios.size();i++){
            if(listaUsuarios.get(i).getNome().equals(usuario) && listaUsuarios.get(i).getSenha().equals(senha)){
                usuarioAtual = listaUsuarios.get(i);
                return true;
            }
        }
        return false;
    }
}
