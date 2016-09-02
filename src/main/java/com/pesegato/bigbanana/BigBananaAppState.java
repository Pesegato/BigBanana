package com.pesegato.bigbanana;


import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.pesegato.bigbanana.sampleapp.BBMapper;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.*;

public class BigBananaAppState extends BaseAppState implements StateFunctionListener {

    public static final String GROUP_BIGBANANA = "group bigbanana";
    public static final String STR_OK = "bigbanana ok";
    public static final String STR_CANCEL = "bigbanana cancel";

    public static final FunctionId F_HORIZONTAL = new FunctionId(GROUP_BIGBANANA, "bigbanana horizontal");
    public static final FunctionId F_VERTICAL = new FunctionId(GROUP_BIGBANANA, "bigbanana vertical");
    public static final FunctionId F_OK = new FunctionId(GROUP_BIGBANANA, STR_OK);
    public static final FunctionId F_CANCEL = new FunctionId(GROUP_BIGBANANA, STR_CANCEL);
    InputMapper inputMapper;

    BigBananaReceiver receiver;

    @Override
    protected void initialize(Application app) {
        BBMapper.init();
        inputMapper = GuiGlobals.getInstance().getInputMapper();
        inputMapper.map(F_HORIZONTAL, InputState.Negative, InputBindings.MOVEH_PAD);
        inputMapper.map(F_HORIZONTAL, InputBindings.MOVERI_K);
        inputMapper.map(F_HORIZONTAL, InputState.Negative, InputBindings.MOVELE_K);
        inputMapper.map(F_VERTICAL, InputState.Negative, InputBindings.MOVEV_PAD);
        inputMapper.map(F_VERTICAL, InputBindings.MOVEUP_K);
        inputMapper.map(F_VERTICAL, InputState.Negative, InputBindings.MOVEDN_K);
        //inputMapper.addAnalogListener(this, F_HORIZONTAL);
        //inputMapper.addAnalogListener(this, F_VERTICAL);
        //inputMapper.addAnalogListener(this, F_ROTATE);
        inputMapper.map(F_OK, InputBindings.OK_PAD);
        inputMapper.map(F_OK, InputBindings.OK_KEY);
        inputMapper.map(F_CANCEL, InputBindings.CANCEL_PAD);
        inputMapper.map(F_CANCEL, InputBindings.CANCEL_KEY);
        inputMapper.addStateListener(this, F_OK);
        inputMapper.addStateListener(this, F_CANCEL);

    }

    public void setReceiver(BigBananaReceiver receiver){
        this.receiver=receiver;
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        inputMapper.activateGroup(GROUP_BIGBANANA);
    }

    @Override
    protected void onDisable() {
        inputMapper.deactivateGroup(GROUP_BIGBANANA);
    }

    @Override
    public void valueChanged(FunctionId fi, InputState is, double d) {
        if (is==InputState.Positive){
            switch (fi.getName()){
                case STR_OK:
                    receiver.pressedOK();
                    break;
                case STR_CANCEL:
                    receiver.pressedCancel();
                    break;
            }
        }
    }
}
