package com.example.rat.primeedu;

import java.io.Serializable;

public class ListOfStudentsIdentity {
    private String StudentsName,Fathers_name,Mothers_Name;
    private String FathersWork,Mothers_Work;
    private String FathersNo,MothersNo;

    public ListOfStudentsIdentity(String studentsName, String fathers_name, String mothers_Name, String fathersWork, String mothers_Work, String fathersNo, String mothersNo) {
        StudentsName = studentsName;
        Fathers_name = fathers_name;
        Mothers_Name = mothers_Name;
        FathersWork = fathersWork;
        Mothers_Work = mothers_Work;
        FathersNo = fathersNo;
        MothersNo = mothersNo;
    }
    public ListOfStudentsIdentity(){

    }

    public String getStudentsName() {
        return StudentsName;
    }

    public void setStudentsName(String studentsName) {
        StudentsName = studentsName;
    }

    public String getFathers_name() {
        return Fathers_name;
    }

    public void setFathers_name(String fathers_name) {
        Fathers_name = fathers_name;
    }

    public String getMothers_Name() {
        return Mothers_Name;
    }

    public void setMothers_Name(String mothers_Name) {
        Mothers_Name = mothers_Name;
    }

    public String getFathersWork() {
        return FathersWork;
    }

    public void setFathersWork(String fathersWork) {
        FathersWork = fathersWork;
    }

    public String getMothers_Work() {
        return Mothers_Work;
    }

    public void setMothers_Work(String mothers_Work) {
        Mothers_Work = mothers_Work;
    }

    public String getFathersNo() {
        return FathersNo;
    }

    public void setFathersNo(String fathersNo) {
        FathersNo = fathersNo;
    }

    public String getMothersNo() {
        return MothersNo;
    }

    public void setMothersNo(String mothersNo) {
        MothersNo = mothersNo;
    }
}
