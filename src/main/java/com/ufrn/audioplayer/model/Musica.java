package com.ufrn.audioplayer.model;
import java.io.*;

public class Musica {

    private int id;
    private File file;

    public Musica(File file){
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


}
