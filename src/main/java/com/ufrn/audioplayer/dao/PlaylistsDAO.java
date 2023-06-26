package com.ufrn.audioplayer.dao;

import com.ufrn.audioplayer.model.Musica;
import com.ufrn.audioplayer.model.Playlist;

import java.io.*;
import java.util.ArrayList;

public class PlaylistsDAO {

    private static PlaylistsDAO pDao;

    private ArrayList<Playlist> playlists;

    private File dir = new File("src/main/resources/com/ufrn/audioplayer/saves/playlists");
    private File[] files = dir.listFiles();

    public PlaylistsDAO() {
        playlists = new ArrayList<Playlist>();
        if(files != null){
            for(File file : files) {
                readFile(file);
            }
        }
    }

    public static PlaylistsDAO getInstance() {
        if (pDao == null) {
            pDao = new PlaylistsDAO();
        }
        return pDao;
    }


    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    /**
     * Lê os arquivos com as playlists
     * @param file
     */
    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = br.readLine();
            ArrayList<Musica> musicas = new ArrayList<Musica>();
            String playlistData[] = line.split(", ");
            int id = Integer.parseInt(playlistData[0]);
            int usr_id = Integer.parseInt(playlistData[1]);
            String nome = playlistData[2];
            line = br.readLine();
            while (line != null && !line.isEmpty()) {
                String playlistData2[] = line.split(", ");
                Musica m = new Musica(new File(playlistData2[1]));
                m.setId(Integer.parseInt(playlistData2[0]));
                musicas.add(m);
                line = br.readLine();
            }
            Playlist list = new Playlist(usr_id,nome,musicas);
            addPlaylist(list);

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    public void addPlaylist(Playlist list){
        playlists.add(list);
        list.setId(playlists.size());
    }

    public void removePlaylist(Playlist list){
        playlists.remove(list);
    }

    /**
     * Salva as playlists em arquivos .txt
     * @param playlist
     */
    public void saveFile(Playlist playlist){
        System.out.println("Nome: "+playlist.getNome());
        try {
            File file = new File(dir, "playlist_"+playlist.getId());
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(playlist.getId()+", "+playlist.getUsr_id()+", "+playlist.getNome());
            for(int i = 0; i<playlist.getMusicas_playlist().size(); i++){
                bufferedWriter.write(playlist.getMusicas_playlist().get(i).getId()+", "+playlist.getMusicas_playlist().get(i).getFile().getPath());
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    /**
     * Adiciona uma música em uma playlist já existente
     * @param p
     * @param m
     */
    public void addMusica(Playlist p,Musica m){
        try {
            FileWriter writer = new FileWriter(dir+"/playlist_"+p.getId(),true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.newLine();
            bufferedWriter.write(m.getId()+", "+m.getFile().getPath());
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }

    }

}
