/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import static com.pesegato.bigbanana.BigBananaAppState.*;

import java.util.ArrayList;

/**
 * @author Pesegato
 */
public class BBBindings {

    static ArrayList<String> keybbmapping = new ArrayList<>();
    static ArrayList<String> padbbmapping = new ArrayList<>();

    public static int getKeySize() {
        return keybbmapping.size();
    }

    public static int getPadSize() {
        return padbbmapping.size();
    }

    /**
     * This method maps game-specific binding to bigbanana number, because
     * bigbanana don't enforce a name but uses reflection
     * <p>
     * like: 'mario.jump' becomes 'bigbanana.keyboard.0'
     *
     * @param name the binding
     */
    public static void addKeyMapping(String name) {
        keybbmapping.add(name);
    }

    public static void addPadMapping(String name) {
        padbbmapping.add(name);
    }

    public static String getMapping(String key) {
        if (key.startsWith("bigbanana.keyboard.")) {
            //int idx = bbmapping.indexOf(key);
            int idx = Integer.parseInt(key.substring(19));
            return keybbmapping.get(idx);
        } else if (key.startsWith("bigbanana.pad.")) {
            //int idx = bbmapping.indexOf(key);
            int idx = Integer.parseInt(key.substring(14));
            return padbbmapping.get(idx);
        }
        return "keyboard." + key;
    }

    public static int getK(String key) {
        int idx = keybbmapping.indexOf(key);
        if (idx > -1) {
            return BIGBANANA_KEYBOARD[idx];
        }
        return -1;
    }
}
