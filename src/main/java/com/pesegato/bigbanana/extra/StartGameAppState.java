package com.pesegato.bigbanana.extra;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.*;

public class StartGameAppState extends BaseAppState implements StateFunctionListener {

    public static final String GROUP_BIGBANANA = "group bigbanana";

    public static final FunctionId F_START = new FunctionId(GROUP_BIGBANANA, "start game");
    InputMapper inputMapper;
    private Class startable = null;

    public StartGameAppState(Class c) {
        this.startable = c;
    }

    @Override
    protected void initialize(Application app) {
        inputMapper = GuiGlobals.getInstance().getInputMapper();
        inputMapper.map(F_START, Button.JOYSTICK_START);
        inputMapper.map(F_START, KeyInput.KEY_K);
        inputMapper.addStateListener(this, F_START);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_BIGBANANA);
    }

    @Override
    protected void onDisable() {
        GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_BIGBANANA);
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (value.equals(InputState.Off)) {
            System.out.println("Pressed " + func);
            setEnabled(false);
            BBStartable st = (BBStartable) getState(startable);
            st.startGame();
        }
    }
}
