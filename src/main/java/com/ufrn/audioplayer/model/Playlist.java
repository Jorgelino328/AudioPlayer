package com.ufrn.audioplayer.model;

import com.ufrn.audioplayer.dao.MusicasDAO;

import java.util.ArrayList;

public class Playlist {
    private int id;
    private int usr_id;
    private String nome;
    private ArrayList<Integer> musicas_playlist;

    public Playlist(int usr_id, String nome, ArrayList<Integer> musicas){
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

    public ArrayList<Integer> getMusicas_playlist() {
        return musicas_playlist;
    }

    public void setMusicas_playlist(ArrayList<Integer> musicas_playlist) {
        this.musicas_playlist = musicas_playlist;
    }

}
