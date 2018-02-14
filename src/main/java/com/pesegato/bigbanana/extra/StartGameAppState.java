package com.pesegato.bigbanana.extra;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.*;

import static com.pesegato.bigbanana.extra.BigBananaFunctions.*;

public class StartGameAppState extends BaseAppState implements StateFunctionListener {

    InputMapper inputMapper;
    private Class startable = null;

    public StartGameAppState(Class c) {
        this.startable = c;
    }

    @Override
    protected void initialize(Application app) {
        inputMapper = GuiGlobals.getInstance().getInputMapper();
        BigBananaFunctions.initializeDefaultMappings(inputMapper);
        //moved to onEnable
        //inputMapper.addStateListener(this, F_START);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        inputMapper.addStateListener(this, F_START);
        GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_BIGBANANA);
    }

    @Override
    protected void onDisable() {
        inputMapper.removeStateListener(this, F_START);
        //GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_BIGBANANA);
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
