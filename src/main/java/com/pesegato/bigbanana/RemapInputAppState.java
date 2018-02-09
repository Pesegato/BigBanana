/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyNames;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.*;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.pesegato.bigbanana.BigBananaAppState.*;

/**
 * @author Pesegato
 */
public class RemapInputAppState extends BaseAppState {

    private Container mainWindow;
    private Container mainWindow2;

    HashMap<ActionButton, BBBind> mappings = new HashMap<>();
    HashMap<String, ActionButton> mappings2 = new HashMap<>();

    ActionButton selected = null;

    public float getStandardScale() {
        int height = getApplication().getCamera().getHeight();
        return height / 720f;
    }

    @Override
    protected void initialize(Application app) {
        mainWindow = new Container();
        mainWindow2 = new Container();

        Label title = mainWindow.addChild(new Label("Remap input key"));
        title.setFontSize(24);
        addKeyboardButton(new BBBind(BB_MOVEUP, "keyboard.", true));
        addKeyboardButton(new BBBind(BB_MOVEDOWN, "keyboard.", true));
        addKeyboardButton(new BBBind(BB_MOVERIGHT, "keyboard.", true));
        addKeyboardButton(new BBBind(BB_MOVELEFT, "keyboard.", true));
        for (int i = 0; i < BBBindings.getKeySize(); i++) {
            addKeyboardButton(new BBBind("bigbanana.keyboard." + i, "keyboard.", false));
        }

        Label title2 = mainWindow2.addChild(new Label("Remap input joy*/mouse"));
        title2.setFontSize(24);
        for (int i = 0; i < BBBindings.getPadSize(); i++) {
            addButtonButton(new BBBind("bigbanana.pad." + i, "pad.", false));
        }

        // Calculate a standard scale and position from the app's camera
        // height
        int height = app.getCamera().getHeight();
        Vector3f pref = mainWindow.getPreferredSize().clone();

        float standardScale = getStandardScale();
        pref.multLocal(1.5f * standardScale);

        // With a slight bias toward the top
        float y = height * 0.6f + pref.y * 0.5f;

        mainWindow.setLocalTranslation(100 * standardScale, y, 0);
        mainWindow.setLocalScale(1.5f * standardScale);
        mainWindow2.setLocalTranslation(500 * standardScale, y, 0);
        mainWindow2.setLocalScale(1.5f * standardScale);

        for (int i = 0; i < 0xff; i++) {
            getApplication().getInputManager().addMapping(i + "", new KeyTrigger(i));
            getApplication().getInputManager().addListener(al, i + "");
        }
        for (int i = 1; i < 4; i++) {
            getApplication().getInputManager().addMapping("mouse_" + i, new MouseButtonTrigger(i - 1));
            getApplication().getInputManager().addListener(al, "mouse_" + i);
        }
    }

    ActionListener al = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (!keyPressed) {
                if (selected != null) {

                    try {
                        BBBind bind = mappings.get(selected);
                        if (name.startsWith("mouse_")) {
                            for (Field field : com.simsilica.lemur.input.Button.class.getFields()) {
                                com.simsilica.lemur.input.Button b = (com.simsilica.lemur.input.Button) field.get(null);
                                if (b.getId().equals(name)) {
                                    System.out.println("pressed " + b.getName());
                                    getState(BigBananaAppState.class).peel.getProperties().setProperty(bind.propertyKey, b.getName());
                                    saveProps();
                                    selected.setText(
                                            bind.name + ": "
                                                    + //name = "22"
                                                    //KeyNames.getName(Integer.parseInt(name)) = "Backspace"
                                                    b.getName()
                                    );
                                    selected = null;
                                    return;
                                }
                            }

                        }
                        int keykey = Integer.parseInt(name);
                        String userFriendlyName = KeyNames.getName(keykey);
                        System.out.println(bind.name + " / " + keykey + " / " + userFriendlyName);

                        getState(BigBananaAppState.class).peel.getProperties().setProperty(bind.propertyKey, userFriendlyName);
                        saveProps();
                        selected.setText(
                                bind.name + ": "
                                        + //name = "22"
                                        //KeyNames.getName(Integer.parseInt(name)) = "Backspace"
                                        userFriendlyName
                        );
                        selected = null;
                    } catch (Exception ex) {
                        Logger.getLogger(RemapInputAppState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    };

    public void saveProps() throws Exception {
        Properties prop = getState(BigBananaAppState.class).peel.getProperties();
        Properties tmp = new Properties() {
            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(new TreeSet<Object>(super.keySet()));
            }
        };
        tmp.putAll(prop);
        tmp.store(new FileWriter(getState(BigBananaAppState.class).peel.getFilePath()), "autogenerated");
    }

    private void setSelectedButton(ActionButton ab) {
        if (selected != null) {
            System.out.println("Can't remap two buttons at once!");
            return;
        }
        selected = ab;
        selected.setText(mappings.get(ab).name + ": ...");
    }

    public void KeyboardMoveUp() {
        setSelectedButton(mappings2.get("keyboard.move.up"));
    }

    public void KeyboardMoveDown() {
        setSelectedButton(mappings2.get("keyboard.move.down"));
    }

    public void KeyboardMoveRight() {
        setSelectedButton(mappings2.get("keyboard.move.right"));
    }

    public void KeyboardMoveLeft() {
        setSelectedButton(mappings2.get("keyboard.move.left"));
    }

    public void BigbananaKeyboard0() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.0"));
    }

    public void BigbananaKeyboard1() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.1"));
    }

    public void BigbananaKeyboard2() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.2"));
    }

    public void BigbananaKeyboard3() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.3"));
    }

    public void BigbananaKeyboard4() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.4"));
    }

    public void BigbananaKeyboard5() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.5"));
    }

    public void BigbananaKeyboard6() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.6"));
    }

    public void BigbananaKeyboard7() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.7"));
    }

    public void BigbananaKeyboard8() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.8"));
    }

    public void BigbananaKeyboard9() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.9"));
    }

    public void BigbananaKeyboard10() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.10"));
    }

    public void BigbananaKeyboard11() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.11"));
    }

    public void BigbananaKeyboard12() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.12"));
    }

    public void BigbananaKeyboard13() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.13"));
    }

    public void BigbananaKeyboard14() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.14"));
    }

    public void BigbananaKeyboard15() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.15"));
    }

    public void BigbananaKeyboard16() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.16"));
    }

    public void BigbananaKeyboard17() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.17"));
    }

    public void BigbananaKeyboard18() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.18"));
    }

    public void BigbananaKeyboard19() {
        setSelectedButton(mappings2.get("bigbanana.keyboard.19"));
    }

    public void BigbananaPad0() {
        setSelectedButton(mappings2.get("bigbanana.pad.0"));
    }

    public void BigbananaPad1() {
        setSelectedButton(mappings2.get("bigbanana.pad.1"));
    }

    public void BigbananaPad2() {
        setSelectedButton(mappings2.get("bigbanana.pad.2"));
    }

    public void BigbananaPad3() {
        setSelectedButton(mappings2.get("bigbanana.pad.3"));
    }

    public void BigbananaPad4() {
        setSelectedButton(mappings2.get("bigbanana.pad.4"));
    }

    public void BigbananaPad5() {
        setSelectedButton(mappings2.get("bigbanana.pad.5"));
    }

    public void BigbananaPad6() {
        setSelectedButton(mappings2.get("bigbanana.pad.6"));
    }

    public void BigbananaPad7() {
        setSelectedButton(mappings2.get("bigbanana.pad.7"));
    }

    public void BigbananaPad8() {
        setSelectedButton(mappings2.get("bigbanana.pad.8"));
    }

    public void BigbananaPad9() {
        setSelectedButton(mappings2.get("bigbanana.pad.9"));
    }

    private ActionButton addKeyboardButton(BBBind key) {
        ActionButton action = mainWindow.addChild(new ActionButton(new CallMethodAction(getMapping(key), this, key.reflect)));
        mappings.put(action, key);
        mappings2.put(key.internalName, action);
        return action;
    }

    private ActionButton addButtonButton(BBBind key) {
        ActionButton action = mainWindow2.addChild(new ActionButton(new CallMethodAction(getMapping(key), this, key.reflect)));
        mappings.put(action, key);
        mappings2.put(key.internalName, action);
        return action;
    }

    private String getMapping(BBBind key) {
        try {
            return key.name + ": "
                    + getState(BigBananaAppState.class).peel.getProperties().get(key.propertyKey) //KeyNames.getName(KeyNames.getIndex((String) BigBananaAppState.props.get(key))) //KeyNames.getIndex((String)BigBananaAppState.props.get(key))
                    ;
        } catch (Exception ex) {
            Logger.getLogger(RemapInputAppState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        Node gui = ((SimpleApplication) getApplication()).getGuiNode();
        gui.attachChild(mainWindow);
        gui.attachChild(mainWindow2);
        GuiGlobals.getInstance().requestFocus(mainWindow);
    }

    @Override
    protected void onDisable() {
        mainWindow.removeFromParent();
        mainWindow2.removeFromParent();
    }

}
