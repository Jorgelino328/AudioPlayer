package com.ufrn.audioplayer.dao;

import java.io.*;
import java.util.ArrayList;

public class DiretoriosDAO {
    ArrayList<File> listaDiretorios;
    private File file = new File("src/main/resources/com/ufrn/audioplayer/saves/diretorios.txt");

    private static DiretoriosDAO dDao;

    public DiretoriosDAO() {
        listaDiretorios = new ArrayList<File>();
        if(file != null){
            readFile(file);
        }
    }

    public ArrayList<File> getListaDiretorios() {
        return listaDiretorios;
    }

    public void setListaDiretorios(ArrayList<File> listaDiretorios) {
        this.listaDiretorios = listaDiretorios;
    }

    /**
     * Lê o arquivo que contém os diretórios salvos
     * @param file
     */
    public void readFile(File file){

        try (InputStream inputStream = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = br.readLine()) != null) {
                File dir_file = new File(line);
                listaDiretorios.add(dir_file);
            }

        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }

    /**
     * Salva os diretórios num arquivo .txt
     * @param dir
     */
    public void saveFile(File dir){
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(dir.getPath());
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println("Erro lendo o arquivo: " + e.getMessage());
        }
    }
    // Singleton
    public static DiretoriosDAO getInstance() {
        if (dDao == null) {
            dDao = new DiretoriosDAO();
        }
        return dDao;
    }

    public void addDiretorio(File dir_file){
        listaDiretorios.add(dir_file);
    }

    public void removeDiretorio(File dir_file){
        listaDiretorios.remove(dir_file);
    }

    public String listarDiretorios() {
        System.out.println("Diretorios:");
        System.out.println("------------------------------------------------");
        String output = "";
        for (int i=0;i<listaDiretorios.size();i++) {
            output += "\tDiretório "+i+" :" + listaDiretorios.get(i)+
                    "\n";
            output += "----------------------------------------------------------\n";
        }
        return output;
    }

}
