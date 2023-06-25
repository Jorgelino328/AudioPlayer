package com.ufrn.audioplayer.model;

import java.util.ArrayList;

public class UsuarioVIP extends Usuario{
    private ArrayList<Musica> playlist;

    public UsuarioVIP(String nome, String senha) {
        super(nome, senha);
    }

    public ArrayList<Musica> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<Musica> playlist) {
        this.playlist = playlist;
    }
}
