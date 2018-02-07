package com.pesegato.bigbanana;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;

public class BBBind {

    String name; //   my.action
    String type; // keyboard.
    String propertyKey; // keyboard.my.action
    String internalName; // bigbanana.keyboard.0
    String reflect; // BigbananaKeyboard0
    int id; //0

    static ArrayList<BBBind> keybbmaps = new ArrayList<>();

    public BBBind(String name, String type, boolean implicit) {
        if (implicit) {
            this.name = name;
            this.type = type;
            propertyKey = type + name;
            this.id = keybbmaps.size();
            this.internalName = propertyKey;
            reflect = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, internalName.replace('.', '_'));
            keybbmaps.add(this);
        } else {
            this.internalName = name;
            this.name = BBBindings.getMapping(name);
            this.type = type;
            propertyKey = type + this.name;
            this.id = keybbmaps.size();
            reflect = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, internalName.replace('.', '_'));
            keybbmaps.add(this);
        }
    }

    public String getCamelCase() {
        return reflect;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getValue() {
        return "";
    }
}
