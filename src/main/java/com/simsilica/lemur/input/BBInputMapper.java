package com.simsilica.lemur.input;

import com.jme3.input.InputManager;

public class BBInputMapper extends InputMapper{

    public BBInputMapper(InputManager inputManager) {
        super(inputManager);
    }

    public void bbnotifyStateChanged(FunctionId f, float tpf, InputState is){
        getFunctionListeners(f,false).notifyStateChanged(f, is);
    }
}


