package com.pesegato.bigbanana.extra;

import com.pesegato.bigbanana.BBFocusTarget;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;

/**
 * BBFocusTarget for Lemur Buttons
 */

public class LBBFocusTarget extends BBFocusTarget {

    @Override
    public void onFocusGain() {
        GuiGlobals.getInstance().requestFocus(spatial);
        System.out.println("LBBFocus gain");
    }

    @Override
    public void onFocusLost() {
    }

    /**
     * Careful, this works only for Buttons!
     */
    @Override
    public boolean isFocusable() {
        return ((Button) spatial).isEnabled();
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        System.out.println("LBBFocus changed");
    }

}
