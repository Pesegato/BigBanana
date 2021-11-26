/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.simsilica.lemur.focus.FocusTarget;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;

/**
 * @author Pesegato
 */
public abstract class BBFocusTarget extends AbstractControl implements FocusTarget {

    boolean isFocused;

    @Override
    public boolean isFocused() {
        return isFocused;
    }

    @Override
    public void focusGained() {
        isFocused = true;
        onFocusGain();
    }

    @Override
    public void focusLost() {
        isFocused = false;
        onFocusLost();
    }

    public abstract void onValueChanged(FunctionId func, InputState value, double tpf);

    public abstract void onFocusGain();

    public abstract void onFocusLost();

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

}
