package com.pesegato.bigbanana.extra;

import com.pesegato.bigbanana.BBFocusTarget;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BBFocusTarget for Lemur Buttons
 */

public class LBBFocusTarget extends BBFocusTarget {

    static Logger log = LoggerFactory.getLogger(LBBFocusTarget.class);

    @Override
    public void onFocusGain() {
        GuiGlobals.getInstance().requestFocus(spatial);
        log.trace("LBBFocus gain");
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
    public void onValueChanged(FunctionId func, InputState value, double tpf) {
        log.trace("LBBFocus changed");
    }

}
