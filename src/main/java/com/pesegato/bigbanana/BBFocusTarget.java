/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.focus.FocusNavigationFunctions;
import com.simsilica.lemur.focus.FocusTarget;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 * @author Pesegato
 */
public abstract class BBFocusTarget extends AbstractControl implements FocusTarget, StateFunctionListener {

    boolean isFocused;

    @Override
    public boolean isFocused() {
        return isFocused;
    }

    @Override
    public void focusGained() {
        GuiGlobals.getInstance().getInputMapper().addStateListener(this, FocusNavigationFunctions.F_ACTIVATE);
        isFocused = true;
        onFocusGain();
    }

    @Override
    public void focusLost() {
        GuiGlobals.getInstance().getInputMapper().removeStateListener(this, FocusNavigationFunctions.F_ACTIVATE);
        isFocused = false;
        onFocusLost();
    }

    public abstract void onFocusGain();

    public abstract void onFocusLost();

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

}
