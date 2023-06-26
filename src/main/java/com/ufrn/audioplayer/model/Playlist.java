package com.ufrn.audioplayer.model;

import com.ufrn.audioplayer.dao.MusicasDAO;

import java.util.ArrayList;

public class Playlist {
    private int id;
    private int usr_id;
    private String nome;
    private ArrayList<Musica> musicas_playlist;

    public Playlist(int usr_id, String nome, ArrayList<Musica> musicas){
        this.usr_id = usr_id;
        this.nome = nome;
        musicas_playlist = musicas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Musica> getMusicas_playlist() {
        return musicas_playlist;
    }

    public void setMusicas_playlist(ArrayList<Musica> musicas_playlist) {
        this.musicas_playlist = musicas_playlist;
    }

}
