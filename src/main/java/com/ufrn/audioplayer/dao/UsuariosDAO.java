package com.ufrn.audioplayer.dao;

import java.util.ArrayList;
import com.ufrn.audioplayer.model.Usuario;

public class UsuariosDAO {
    ArrayList<Usuario> listaUsuarios;

    private static UsuariosDAO uDao;

    public UsuariosDAO() {
        listaUsuarios = new ArrayList<Usuario>();
        //uDao.addUsuario("Joao");
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
            if(listaUsuarios.get(i).getNome() == usuario && listaUsuarios.get(i).getSenha() == senha){
                return true;
            }
        }
        return false;
    }
}
