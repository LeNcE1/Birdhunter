package com.example.lence.bird_hunter.ui;



import com.example.lence.bird_hunter.model.Birds;

import java.util.ArrayList;
import java.util.List;

public interface MVPDB {
//    void insert(UserModel insertUser);
    List<String> getBirds();
//    void delete(String id);
    void upDate(ArrayList<String> birds);
}
