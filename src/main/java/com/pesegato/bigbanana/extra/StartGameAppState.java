package com.pesegato.bigbanana.extra;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

public class StartGameAppState extends BaseAppState implements StateFunctionListener {

    InputMapper inputMapper;
    private Class startable = null;

    public StartGameAppState(Class c) {
        this.startable = c;
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
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
