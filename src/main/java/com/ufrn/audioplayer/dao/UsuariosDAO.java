package com.ufrn.audioplayer.dao;

import java.io.*;
import java.util.ArrayList;
import com.ufrn.audioplayer.model.Usuario;
import com.ufrn.audioplayer.model.UsuarioVIP;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    /**
     * Lẽ o arquivo onde os dados dos usuários estão salvos
     * @param file
     */
    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(", ");
                int id = Integer.parseInt(userData[0]);
                String name = userData[1];
                String senha = userData[2];
                boolean isVIP = Boolean.parseBoolean(userData[3]);
                Usuario new_usr;
                if(isVIP){
                    new_usr = new UsuarioVIP(name, senha);
                }else{
                    new_usr = new Usuario(name, senha);
                }
                addUsuario(new_usr);
            }

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    /**
     * Salva os dados dos usuários em um arquivo .txt
     * @param usuario
     */
    public void saveFile(Usuario usuario){
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            if (file.length() > 0) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(usuario.getId()+", "+usuario.getNome()+", "+hashPassword(usuario.getSenha())+", "+(usuario instanceof UsuarioVIP));
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

    /**
     * Valida o usuário tentando logar no sistema
     * @param usuario
     * @param senha
     * @return
     */
    public boolean validaUsuario(String usuario, String senha){

        for(int i=0; i<listaUsuarios.size();i++){
            if(listaUsuarios.get(i).getNome().equals(usuario) && listaUsuarios.get(i).getSenha().equals(hashPassword(senha))){
                usuarioAtual = listaUsuarios.get(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Utiliza do hash SHA-256 pra criptografar as senhas
     * @param password
     * @return
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception
            e.printStackTrace();
            return null;
        }
    }

}
