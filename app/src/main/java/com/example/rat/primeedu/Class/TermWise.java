package com.example.rat.primeedu.Class;

import android.util.Pair;

import java.util.ArrayList;

public class TermWise {
    private String term;
    private ArrayList< Pair<String,Pair<String,String>>>Subjects;

    public TermWise(String term, ArrayList<Pair<String, Pair<String, String>>> subjects) {
        this.term = term;
        Subjects = subjects;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public ArrayList<Pair<String, Pair<String, String>>> getSubjects() {
        return Subjects;
    }

    public void setSubjects(ArrayList<Pair<String, Pair<String, String>>> subjects) {
        Subjects = subjects;
    }
}

