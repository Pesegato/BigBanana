/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.KeyNames;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.focus.FocusNavigationState;
import com.simsilica.lemur.input.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static com.pesegato.bigbanana.BBInput.*;
import static com.pesegato.bigbanana.extra.BigBananaFunctions.F_BACK;
import static com.pesegato.bigbanana.extra.BigBananaFunctions.GROUP_BIGBANANA;
import static com.simsilica.lemur.focus.FocusNavigationFunctions.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Pesegato
 */
public class BigBananaAppState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(BigBananaAppState.class);

    private static float DEADZONE = 0.02f;

    public static final String BB_MOVEUP = "move.up";
    public static final String BB_MOVEDOWN = "move.down";
    public static final String BB_MOVERIGHT = "move.right";
    public static final String BB_MOVELEFT = "move.left";
    public static final String BB_MOVE_HORIZONTAL = "move.horizontal";
    public static final String BB_MOVE_VERTICAL = "move.vertical";

    int prevstate[] = new int[15];
    int prevstateAxesDpad[] = new int[2];
    int prevstateDpadAxes[] = new int[2];
    float prevstateAxes[] = new float[6];

    //public static Axis PAD_MOVE_VERTICAL;
    //public static Axis PAD_MOVE_HORIZONTAL;
    public static final FunctionId LEFT_STICK_X = new FunctionId("bb.left.stick.x");
    public static final FunctionId LEFT_STICK_Y = new FunctionId("bb.left.stick.y");
    public static final FunctionId RIGHT_STICK_X = new FunctionId("bb.right.stick.x");
    public static final FunctionId RIGHT_STICK_Y = new FunctionId("bb.right.stick.y");

    public static int KEYBOARD_MOVE_UP;
    public static int KEYBOARD_MOVE_DOWN;
    public static int KEYBOARD_MOVE_RIGHT;
    public static int KEYBOARD_MOVE_LEFT;

    public static int BIGBANANA_KEYBOARD[];
    public static BBInput BIGBANANA_BUTTON[];

    HashMap<FunctionId, StateFunctionListener> stateFunctionListeners = new HashMap<>();
    HashMap<FunctionId, AnalogFunctionListener> analogFunctionListeners = new HashMap<>();
    HashMap<BBInput, FunctionId> glfwMap = new HashMap<>();

    InputMapper inputMapper;
    GLFWGamepadState state;
    BigBananaPeel peel;

    boolean bananaful = false;
    boolean invertLX = false;
    boolean invertLY = false;
    boolean useLeftStickAsDpad = true;
    boolean useKeysAsLeftStick = true;

    public BigBananaAppState(BigBananaPeel peel) {
        this.peel = peel;
        setUseLeftStickAsDpad(true);
        setInvertLeftStickY(true);
    }

    private int getKeyboardInput(String key, String deflt) {
        log.trace("Loading settings for {}", key);
        String val = peel.getProperties().getProperty("keyboard." + key, deflt);
        for (int i = 0; i < 0xff; i++) {
            String keyname = KeyNames.getName(i);
            if ((keyname != null) && (keyname.equals(val))) {
                log.info("{} -> {}", key, val);
                return i;
            }
        }
        //return KeyInput.class.getField(props.getProperty(key, deflt)).getInt(null);
        return KeyInput.KEY_UNKNOWN;
    }

    private BBInput getButtonInput(String key, BBInput deflt) throws IllegalAccessException {
        log.trace("Loading settings for {}", key);
        String val = peel.getProperties().getProperty("pad." + key, deflt.getName());
        for (Field field : BBInput.class.getFields()) {
            BBInput input = (BBInput) field.get(null);
            if (input.getName().equals(val)) {
                log.info("{} -> {}", key, val);
                return input;
            }
        }
        return null;
    }

    /*private Axis getAxisInput(String key, String deftl) throws IllegalAccessException {
        String val = peel.getProperties().getProperty("pad." + key, deftl);
        Joystick joysticks[] = getApplication().getInputManager().getJoysticks();
        if (joysticks.length > 0) {

            //we use only joystick 1 for now!
            Joystick joystick = joysticks[0];
            for (JoystickAxis axis : joystick.getAxes()) {
                if (axis.getName().equals(val))
                    return axis;
            }
        }
        return null;
    }*/

    public void setDeadZone(float deadzone) {
        DEADZONE = deadzone;
    }

    public void setInvertLeftStickX(boolean invertLX) {
        this.invertLX = invertLX;
    }

    public void setUseLeftStickAsDpad(boolean b) {
        useLeftStickAsDpad = b;
    }

    public void setInvertLeftStickY(boolean invertLY) {
        this.invertLY = invertLY;
    }

    public void addAnalogListener(AnalogFunctionListener listener, FunctionId id) {
        if (id == null) {
            throw new RuntimeException("No function IDs specified.");
        }
        if (listener == null) {
            analogFunctionListeners.remove(id);
        }

        analogFunctionListeners.put(id, listener);
    }

    public void removeAnalogListener(AnalogFunctionListener listener, FunctionId id) {
        if (id == null) {
            throw new RuntimeException("No function IDs specified.");
        }
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        analogFunctionListeners.remove(id);
    }

    public void addStateListener(StateFunctionListener listener, FunctionId id) {
        inputMapper.addStateListener(listener, id);
        addStateListenerBB(listener, id);
    }

    public void removeStateListener(StateFunctionListener listener, FunctionId id) {
        if (listener == null)
            return;
        inputMapper.removeStateListener(listener, id);
        removeStateListenerBB(listener, id);
    }

    private void addStateListenerBB(StateFunctionListener l, FunctionId id) {
        if (id == null) {
            throw new RuntimeException("No function IDs specified.");
        }
        if (l == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        stateFunctionListeners.put(id, l);
    }

    private void removeStateListenerBB(StateFunctionListener l, FunctionId id) {
        if (id == null) {
            throw new RuntimeException("No function IDs specified.");
        }
        if (l == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }

        stateFunctionListeners.remove(id);
    }

    public void activateGroup(String group) {
        inputMapper.activateGroup(group);
    }

    public void deactivateGroup(String group) {
        inputMapper.deactivateGroup(group);
    }

    public void mapBack(StateFunctionListener listener) {
        addStateListener(listener, F_BACK);
        glfwMap.put(BB_BUTTON_BACK, F_BACK);
    }

    public void map(FunctionId id, String key, StateFunctionListener listener) {
        inputMapper.map(id, BBBindings.getK(key));
        addStateListener(listener, id);
        glfwMap.put(BBBindings.getP(key), id);
    }

    public void activate(AnalogFunctionListener analogFunctionListener, StateFunctionListener stateFunctionListener) {
        mapLeftStickX(analogFunctionListener);
        mapLeftStickY(analogFunctionListener);
        setInvertLeftStickY(true);
        GuiGlobals.getInstance().getInputMapper().addAnalogListener(analogFunctionListener, LEFT_STICK_X);
        GuiGlobals.getInstance().getInputMapper().addAnalogListener(analogFunctionListener, LEFT_STICK_Y);
        addStateListener(stateFunctionListener, F_BACK);
        activateGroup(GROUP_BIGBANANA);
    }

    public void deactivate() {
        AnalogFunctionListener listener = analogFunctionListeners.get(LEFT_STICK_X);
        if (listener != null) {
            GuiGlobals.getInstance().getInputMapper().removeAnalogListener(listener, LEFT_STICK_X);
            GuiGlobals.getInstance().getInputMapper().removeAnalogListener(listener, LEFT_STICK_Y);
        }
        removeStateListener(stateFunctionListeners.get(F_BACK), F_BACK);
        mapLeftStickX(null);
        mapLeftStickY(null);
        deactivateGroup(GROUP_BIGBANANA);
    }

    public void enableUINavigationMode(boolean enable) {
        if (enable) {
            getApplication().getInputManager().setCursorVisible(true);
            GuiGlobals.getInstance().getInputMapper().activateGroup(UI_NAV);
        } else {
            getApplication().getInputManager().setCursorVisible(false);
            GuiGlobals.getInstance().getInputMapper().deactivateGroup(UI_NAV);
        }
    }

    /**
     * Map the listener. Set it to null to remove.
     *
     * @param listener
     */

    public void mapLeftStickX(AnalogFunctionListener listener) {
        addAnalogListener(listener, LEFT_STICK_X);
    }

    /**
     * Map the listener. Set it to null to remove.
     *
     * @param listener
     */

    public void mapLeftStickY(AnalogFunctionListener listener) {
        addAnalogListener(listener, LEFT_STICK_Y);
    }

    /**
     * Map the listener. Set it to null to remove.
     *
     * @param listener
     */

    public void mapRightStickX(AnalogFunctionListener listener) {
        addAnalogListener(listener, RIGHT_STICK_X);
    }

    /**
     * Map the listener. Set it to null to remove.
     *
     * @param listener
     */

    public void mapRightStickY(AnalogFunctionListener listener) {
        addAnalogListener(listener, RIGHT_STICK_Y);
    }


    @Override
    protected void initialize(Application app) {
        inputMapper = GuiGlobals.getInstance().getInputMapper();
        getStateManager().detach(getState(FocusNavigationState.class));
        getStateManager().attach(new BananaNavigationState(inputMapper, GuiGlobals.getInstance().getFocusManagerState()));
        if (GLFW.glfwJoystickIsGamepad(GLFW_JOYSTICK_1)) {
            log.info("Gamepad 1 is present, entering real Bananaful mode");
            bananaful = true;
        } else {
            log.info("No Gamepad present, entering Bananaless mode");
            bananaful = false;
        }
        state = new GLFWGamepadState(ByteBuffer.allocateDirect(128));

        try {
            BigBananaAppState.KEYBOARD_MOVE_UP = getKeyboardInput(BB_MOVEUP, peel.getDefaultKeyBind(BB_MOVEUP));
            BigBananaAppState.KEYBOARD_MOVE_DOWN = getKeyboardInput(BB_MOVEDOWN, peel.getDefaultKeyBind(BB_MOVEDOWN));
            BigBananaAppState.KEYBOARD_MOVE_RIGHT = getKeyboardInput(BB_MOVERIGHT, peel.getDefaultKeyBind(BB_MOVERIGHT));
            BigBananaAppState.KEYBOARD_MOVE_LEFT = getKeyboardInput(BB_MOVELEFT, peel.getDefaultKeyBind(BB_MOVELEFT));

            //BigBananaAppState.PAD_MOVE_VERTICAL = getAxisInput(BB_MOVE_VERTICAL, peel.getDefaultAxisBind(BB_MOVE_VERTICAL));
            //BigBananaAppState.PAD_MOVE_HORIZONTAL = getAxisInput(BB_MOVE_HORIZONTAL, peel.getDefaultAxisBind(BB_MOVE_HORIZONTAL));

            BIGBANANA_KEYBOARD = new int[BBBindings.getKeySize()];
            BIGBANANA_BUTTON = new BBInput[BBBindings.getPadSize()];
            for (int i = 0; i < BBBindings.getKeySize(); i++) {
                String action = BBBindings.keybbmapping.get(i);
                BIGBANANA_KEYBOARD[i] = getKeyboardInput(action, peel.getDefaultKeyBind(action));
            }
            for (int i = 0; i < BBBindings.getPadSize(); i++) {
                String action = BBBindings.padbbmapping.get(i);
                BIGBANANA_BUTTON[i] = getButtonInput(action, peel.getDefaultPadBind(action));
            }
        } catch (Exception e) {
            log.error(null, e);
            e.printStackTrace();
        }
        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
        //inputMapper.map(F_X_AXIS, InputState.Negative, PAD_MOVE_VERTICAL);
        inputMapper.map(F_X_AXIS, KEYBOARD_MOVE_RIGHT);
        inputMapper.map(F_X_AXIS, InputState.Negative, KEYBOARD_MOVE_LEFT);
        //inputMapper.map(F_Y_AXIS, InputState.Negative, PAD_MOVE_HORIZONTAL);
        inputMapper.map(F_Y_AXIS, KEYBOARD_MOVE_UP);
        inputMapper.map(F_Y_AXIS, InputState.Negative, KEYBOARD_MOVE_DOWN);

        if (useKeysAsLeftStick) {
            inputMapper.map(LEFT_STICK_X, KEYBOARD_MOVE_RIGHT);
            inputMapper.map(LEFT_STICK_X, InputState.Negative, KEYBOARD_MOVE_LEFT);
            inputMapper.map(LEFT_STICK_Y, KEYBOARD_MOVE_UP);
            inputMapper.map(LEFT_STICK_Y, InputState.Negative, KEYBOARD_MOVE_DOWN);
        }
    }

    public void update(float tpf) {

        if (bananaful) {

            if (GLFW.glfwGetGamepadState(GLFW_JOYSTICK_1, state)) {

                float leftX = manageInput(state, GLFW_GAMEPAD_AXIS_LEFT_X, LEFT_STICK_X, invertLX, tpf);
                float leftY = manageInput(state, GLFW_GAMEPAD_AXIS_LEFT_Y, LEFT_STICK_Y, invertLY, tpf);

                manageInput(state, GLFW_GAMEPAD_BUTTON_A, BB_BUTTON_A, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_B, BB_BUTTON_B, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_X, BB_BUTTON_X, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_Y, BB_BUTTON_Y, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_START, BB_BUTTON_START, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_BACK, BB_BUTTON_BACK, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_LEFT_BUMPER, BB_BUTTON_LBU, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER, BB_BUTTON_RBU, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_LEFT_THUMB, BB_BUTTON_LTH, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_RIGHT_THUMB, BB_BUTTON_RTH, tpf);

                if (useLeftStickAsDpad) {
                    int newstate = 0;
                    if (leftX > 0.5)
                        newstate = 1;
                    if (leftX < -0.5)
                        newstate = -1;
                    if (newstate != prevstateAxesDpad[0]) {
                        switch (newstate) {
                            case 1:
                                pressed(F_X_AXIS, tpf, InputState.Positive);
                                break;
                            case 0:
                                pressed(F_X_AXIS, tpf, InputState.Off);
                                break;
                            case -1:
                                pressed(F_X_AXIS, tpf, InputState.Negative);
                                break;
                        }
                        prevstateAxesDpad[0] = newstate;
                    }

                    newstate = 0;
                    if (leftY > 0.5)
                        newstate = 1;
                    if (leftY < -0.5)
                        newstate = -1;
                    if (newstate != prevstateAxesDpad[1]) {
                        switch (newstate) {
                            case 1:
                                pressed(F_Y_AXIS, tpf, InputState.Positive);
                                break;
                            case 0:
                                pressed(F_Y_AXIS, tpf, InputState.Off);
                                break;
                            case -1:
                                pressed(F_Y_AXIS, tpf, InputState.Negative);
                                break;
                        }
                        prevstateAxesDpad[1] = newstate;
                    }
                }

                manageInput(state, GLFW_GAMEPAD_BUTTON_DPAD_RIGHT, F_X_AXIS, InputState.Positive, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_DPAD_LEFT, F_X_AXIS, InputState.Negative, tpf);
                if (leftX < DEADZONE) {
                    AnalogFunctionListener a = analogFunctionListeners.get(LEFT_STICK_X);
                    if (state.buttons(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW_PRESS) {
                        if (1 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X]) {
                            if (a != null) {
                                a.valueActive(LEFT_STICK_X, 1.0, tpf);
                            }
                            prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X] = 1;
                        }
                    } else if (state.buttons(GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW_PRESS) {
                        if (-1 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X]) {
                            if (a != null) {
                                a.valueActive(LEFT_STICK_X, -1.0, tpf);
                            }
                            prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X] = -1;
                        }
                    } else if (0 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X]) {
                        if (a != null) {
                            a.valueActive(LEFT_STICK_X, 0, tpf);
                        }
                        prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_X] = 0;
                    }
                }

                manageInput(state, GLFW_GAMEPAD_BUTTON_DPAD_UP, F_Y_AXIS, InputState.Positive, tpf);
                manageInput(state, GLFW_GAMEPAD_BUTTON_DPAD_DOWN, F_Y_AXIS, InputState.Negative, tpf);

                if (leftY < DEADZONE) {
                    AnalogFunctionListener a = analogFunctionListeners.get(LEFT_STICK_Y);
                    if (state.buttons(GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW_PRESS) {
                        if (1 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y]) {
                            if (a != null) {
                                a.valueActive(LEFT_STICK_Y, 1.0, tpf);
                            }
                            prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y] = 1;
                        }
                    } else if (state.buttons(GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW_PRESS) {
                        if (-1 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y]) {
                            if (a != null) {
                                a.valueActive(LEFT_STICK_Y, -1.0, tpf);
                            }
                            prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y] = -1;
                        }
                    } else if (0 != prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y]) {
                        if (a != null) {
                            a.valueActive(LEFT_STICK_Y, 0, tpf);
                        }
                        prevstateDpadAxes[GLFW_GAMEPAD_AXIS_LEFT_Y] = 0;
                    }
                }
            }
            /*
            if (state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP) == GLFW.GLFW_PRESS) {
                System.out.println("Pressed D UP!");
                pressed(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP, tpf);
            }
            if (state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT) == GLFW.GLFW_PRESS) {
                System.out.println("Pressed D RIGHT!");
                pressed(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT, tpf);
            }
            if (state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN) == GLFW.GLFW_PRESS) {
                System.out.println("Pressed D DOWN!");
                pressed(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN, tpf);
            }
            if (state.buttons(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT) == GLFW.GLFW_PRESS) {
                System.out.println("Pressed D LEFT!");
                pressed(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT, tpf);
            }
*/
        }

    }

    private float manageInput(GLFWGamepadState state, int in, FunctionId fid, boolean invert, float tpf) {
        float newstate = state.axes(in);
        if (invert) {
            newstate = -newstate;
        }
        if (Math.abs(newstate) < DEADZONE) {
            newstate = 0;
        }
        if (newstate != prevstateAxes[in]) {
            prevstateAxes[in] = newstate;
            AnalogFunctionListener l = analogFunctionListeners.get(fid);
            if (l == null)
                return 0;
            l.valueActive(fid, newstate, tpf);
        }
        return newstate;
    }

    private void manageInput(GLFWGamepadState state, int in, FunctionId fid, InputState is, float tpf) {
        int newstate = state.buttons(in);
        if (newstate != prevstate[in]) {
            switch (newstate) {
                case GLFW_PRESS:
                    pressed(fid, tpf, is);
                    break;
                case GLFW_RELEASE:
                    pressed(fid, tpf, InputState.Off);
                    break;
            }
            prevstate[in] = newstate;
        }
    }

    private void manageInput(GLFWGamepadState state, int in, BBInput bin, float tpf) {
        int newstate = state.buttons(in);
        if (newstate != prevstate[in]) {
            switch (newstate) {
                case GLFW_PRESS:
                    //log.trace("Pressed {}", bin.getName());
                    pressed(bin, tpf, InputState.Positive);
                    if (in == GLFW_GAMEPAD_BUTTON_A) {
                        Spatial s = GuiGlobals.getInstance().getCurrentFocus();
                        System.out.println(s);
                        if (s != null) {
                            for (int i = 0; i < s.getNumControls(); i++) {
                                Control a = s.getControl(i);
                                if (a instanceof MouseEventControl) {
                                    ((MouseEventControl) a).mouseButtonEvent(new MouseButtonEvent(1, true, 0, 0), s, s);
                                } else if (a instanceof StateFunctionListener) {
                                    ((StateFunctionListener) a).valueChanged(F_ACTIVATE, InputState.Positive, tpf);
                                }
                            }
                        }
                    }
                    break;
                case GLFW_RELEASE:
                    //log.trace("Released {}", bin.getName());
                    pressed(bin, tpf, InputState.Off);
                    if (in == GLFW_GAMEPAD_BUTTON_A) {
                        Spatial s = GuiGlobals.getInstance().getCurrentFocus();
                        if (s != null) {
                            for (int i = 0; i < s.getNumControls(); i++) {
                                Control a = s.getControl(i);
                                if (a instanceof MouseEventControl) {
                                    ((MouseEventControl) a).mouseButtonEvent(new MouseButtonEvent(1, false, 0, 0), s, s);
                                } else if (a instanceof StateFunctionListener) {
                                    ((StateFunctionListener) a).valueChanged(F_ACTIVATE, InputState.Off, tpf);
                                }
                            }
                        }
                    }
                    break;
            }
            prevstate[in] = newstate;
        }
    }

    private void pressed(BBInput input, float tpf, InputState is) {
        FunctionId f = glfwMap.get(input);
        if (f == null)
            return;
        StateFunctionListener l = stateFunctionListeners.get(f);
        if (l == null)
            return;
        l.valueChanged(f, is, tpf);
    }

    private void pressed(FunctionId f, float tpf, InputState is) {
        GuiGlobals.getInstance().getInputMapper().listenerMap.get(f).notifyStateChanged(f, is);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        GuiGlobals.getInstance().getInputMapper().activateGroup(UI_NAV);
    }

    @Override
    protected void onDisable() {
        GuiGlobals.getInstance().getInputMapper().deactivateGroup(UI_NAV);
    }
}
