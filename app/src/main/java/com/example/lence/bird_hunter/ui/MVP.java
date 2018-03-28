package com.example.lence.bird_hunter.ui;


import java.util.ArrayList;
import java.util.List;

public interface MVP {
    void addBirds(List<String> birds);
    void showError();
    void show(String res);
    void clear();
}
