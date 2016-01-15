package com.github.lionboard.service;

import com.github.lionboard.model.TempModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lion.k on 16. 1. 12..
 */

@Service
public class IndexServiceDefault implements IndexService {
    @Override
    public List<TempModel> getPosts() {
        List<TempModel> list = new ArrayList<TempModel>();

        list.add(new TempModel(1, "Kang DDan DDan","Hello world !"));
        list.add(new TempModel(2, "Kang DDan DDan","anybody here ?"));

        Collections.sort(list, new idDescCompare());

        return list;
    }


    static class idDescCompare implements Comparator<TempModel> {

        @Override
        public int compare(TempModel arg0, TempModel arg1) {
            // TODO Auto-generated method stub
            return Integer.toString(arg1.getId()).compareTo(Integer.toString(arg0.getId()));
        }

    }
}
