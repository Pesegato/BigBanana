package com.pesegato.bigbanana;


import com.jme3.scene.Geometry;

public interface FocusHandler {

    void focus();
    void unfocus();
    void press();
    Geometry getGeometry();
}
