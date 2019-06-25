package com.primeedu.rat.primeedu.Class;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.Comparator;

public class SortByName implements Comparator<Pair<String,TeacherDetails> > {

    @Override
    public int compare(Pair<String, TeacherDetails> o1, Pair<String, TeacherDetails> o2) {
        String s1 = o1.second.getTeachername().toLowerCase();
        String s2 = o2.second.getTeachername().toLowerCase();

        return (-1)*s1.compareTo(s2);
    }
}
