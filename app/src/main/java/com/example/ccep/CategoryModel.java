package com.example.ccep;


public class CategoryModel {
    private String name;
    private int sets;

    public CategoryModel(){

        //for FireBase
    }
    public CategoryModel(String name, int sets) {
        this.name = name;
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }
}