package com.ufrn.audioplayer.dao;

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

    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                ArrayList<Integer> musicas = new ArrayList<Integer>();
                String playlistData[] = line.split(", ");
                int id = Integer.parseInt(playlistData[0]);
                int usr_id = Integer.parseInt(playlistData[1]);
                String nome = playlistData[2];
                for(int i = 3; i< playlistData.length;i++){
                    musicas.add(Integer.parseInt(playlistData[i]));
                }
                Playlist list = new Playlist(usr_id,nome,musicas);
                addPlaylist(list);
            }

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

    public void saveFile(Playlist playlist){
        try {
            File file = new File(dir, "playlist_"+playlist.getId());
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.newLine();
            String output = playlist.getId()+", "+playlist.getNome();

            for(int i = 0; i<playlist.getMusicas_playlist().size(); i++){
                output+=", "+playlist.getMusicas_playlist().get(i);
            }
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }


}
