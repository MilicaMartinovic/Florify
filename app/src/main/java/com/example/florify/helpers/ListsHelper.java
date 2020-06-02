package com.example.florify.helpers;

import java.util.ArrayList;

public class ListsHelper<T> {
    public ArrayList<T> intersectTwoLists(ArrayList<T> l1, ArrayList<T> l2) {
        ArrayList<T> rtnList = new ArrayList<>();
        for(T dto : l1) {
            if(l2.contains(dto)) {
                rtnList.add(dto);
            }
        }
        return rtnList;
    }
}
