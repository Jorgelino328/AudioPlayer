package com.ufrn.audioplayer.dao;

import com.ufrn.audioplayer.model.Musica;

import java.io.*;
import java.util.ArrayList;

public class MusicasDAO {
    ArrayList<Musica> listaMusicas;
    private File file = new File("src/main/resources/com/ufrn/audioplayer/saves/musicas.txt");

    private static MusicasDAO mDao;

    public MusicasDAO() {
        listaMusicas = new ArrayList<Musica>();
        if(file != null){
            readFile(file);
        }
    }

    public ArrayList<Musica> getListaMusicas() {
        return listaMusicas;
    }

    public void setListaMusicas(ArrayList<Musica> listaMusicas) {
        this.listaMusicas = listaMusicas;
    }

    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String mus_Data[] = line.split(", ");
                int mus_id = Integer.parseInt(mus_Data[0]);
                File mus_file = new File(mus_Data[1]);
                Musica new_mus = new Musica(mus_file);
                new_mus.setId(mus_id);
                addMusica(new_mus);
            }

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    public void saveFile(Musica musica){
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write((listaMusicas.size()+1) +", "+musica.getFile().getPath());
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }
    // Singleton
    public static MusicasDAO getInstance() {
        if (mDao == null) {
            mDao = new MusicasDAO();
        }
        return mDao;
    }

    public void addMusica(Musica musica){
        listaMusicas.add(musica);
    }

    public void removeMusica(Musica Musica){
        listaMusicas.remove(Musica);
    }

    public String listarMusicas() {
        System.out.println("Musicas:");
        System.out.println("------------------------------------------------");
        String output = "";
        for (Musica m : listaMusicas) {
            output += "\tId: " + m.getId() +
                    "\n";
            output += "\tNome: " + m.getFile().getName() +
                    "\n";
            output += "\tPath: " + m.getFile() +
                    "\n";
            output += "----------------------------------------------------------\n";
        }
        return output;
    }
}
