package com.simsilica.lemur.input;

import com.jme3.app.Application;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is a workaround for making BigBanana compatible with standard Lemur.
 * In order to work, requires Lemur to be initialized with:
 *
 * BBGuiGlobals.initialize(this);
 *
 * instead of:
 *
 * GuiGlobals.initialize(this);
 *
 */

public class BBGuiGlobals extends GuiGlobals {
    BBInputMapper bbinputMapper;

    public static void initialize( Application app ) {
        GuiGlobals.setInstance(new BBGuiGlobals(app));
    }

    private BBGuiGlobals(Application app) {
        super(app);
        this.bbinputMapper = new BBInputMapper(app.getInputManager());
    }

    public InputMapper getInputMapper() {
        return bbinputMapper;
    }
}
