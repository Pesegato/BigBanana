/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import java.util.ArrayList;

/**
 *
 * @author Pesegato
 */
public class BigBananaMapping {

    static ArrayList<String> bbmapping = new ArrayList<>();

    public static int getSize() {
        return bbmapping.size();
    }

    public static void addMapping(String name) {
        bbmapping.add(name);
    }

    public static int getKey(String key) {
        if (key.startsWith("bigbanana.keyboard.")) {
            //int idx = bbmapping.indexOf(key);
            return Integer.parseInt(key.substring(19));
        }
        return -1;
    }

    public static String getMapping(String key) {
        if (key.startsWith("bigbanana.keyboard.")) {
            //int idx = bbmapping.indexOf(key);
            int idx = Integer.parseInt(key.substring(19));
            return bbmapping.get(idx);
        }
        return key;
    }
}
