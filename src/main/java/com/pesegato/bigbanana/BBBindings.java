/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import static com.pesegato.bigbanana.BigBananaAppState.*;
import java.util.ArrayList;

/**
 *
 * @author Pesegato
 */
public class BBBindings {

    static ArrayList<String> bbmapping = new ArrayList<>();

    public static int getSize() {
        return bbmapping.size();
    }

    /**
     * This method maps game-specific binding to bigbanana number, because
     * bigbanana don't enforce a name but uses reflection
     *
     * like: 'mario.jump' becomes 'bigbanana.keyboard.0'
     *
     * @param name the binding
     */
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

    public static int get(String key) {
        int idx = bbmapping.indexOf(key);
        if (idx > -1) {
            return BIGBANANA_KEYBOARD[idx];
        }
        return -1;
    }
}
