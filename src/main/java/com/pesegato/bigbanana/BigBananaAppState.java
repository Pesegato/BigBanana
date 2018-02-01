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
import com.simsilica.lemur.GuiGlobals;

import static com.simsilica.lemur.focus.FocusNavigationFunctions.*;

import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Shell32Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Pesegato
 */
public class BigBananaAppState extends BaseAppState implements StateFunctionListener {

    static Logger log = LoggerFactory.getLogger(BigBananaAppState.class);
    public static boolean COOKED_BUILD;
    public static String BUILD_REVISION;
    public static String BUILD_DATE;

    public static final FunctionId F_RED_B = new FunctionId(UI_NAV, "Red B");

    public static Axis PAD_MOVE_VERTICAL;
    public static Axis PAD_MOVE_HORIZONTAL;
    public static int KEYBOARD_MOVE_UP;
    public static int KEYBOARD_MOVE_DOWN;
    public static int KEYBOARD_MOVE_RIGHT;
    public static int KEYBOARD_MOVE_LEFT;

    public static Button PAD_GREEN_A;
    public static Button PAD_RED_B;
    public static Button PAD_BLUE_X;
    public static Button PAD_YELLOW_Y;
    public static Button PAD_START;
    public static Button PAD_HOME;
    public static Button PAD_BACK;
    public static int KEYBOARD_GREEN_A;
    public static int KEYBOARD_RED_B;
    public static int KEYBOARD_BLUE_X;
    public static int KEYBOARD_YELLOW_Y;
    public static int KEYBOARD_START;
    public static int KEYBOARD_HOME;
    public static int KEYBOARD_BACK;

    public static Properties props = new Properties();
    public static String defPath;
    public static String mod = null;

    public static final void preInit(String folderName, String fileName, ClassLoader cl) throws Exception {
        if (COOKED_BUILD) {
            BUILD_REVISION = "cooked build";
            BUILD_DATE = "now";
        } else {
            try {
                Properties prop = new Properties();
                String propFileName = "buildinfo.properties";
                InputStream inputStream = cl.getResourceAsStream(propFileName);
                if (inputStream != null) {
                    prop.load(inputStream);
                } else {
                    throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
                }
                BUILD_REVISION = prop.getProperty("build.revision");
                BUILD_DATE = prop.getProperty("build.date");
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(BigBananaAppState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        log.info("build revision: " + BUILD_REVISION);
        log.info("build date: " + BUILD_DATE);

        props = getProperties(folderName, fileName);
    }

    public static Properties getProperties(String folderName, String fileName) throws Exception {
        try {

            defPath = "~/." + folderName + "/";
            if (System.getProperty("os.name").startsWith("Windows")) {
                defPath = Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_SavedGames) + "/" + folderName + "/";
            }
            new File(defPath + "/slots/").mkdirs();
            File userSettings = new File(defPath + fileName + ".properties");
            File f = new File(defPath);
            if (!userSettings.exists()) {
                f.mkdirs();
                InputStream in = null;//GameGlobals.class.getResourceAsStream(COOKED_BUILD ? "" : "/" + fileName + ".properties");
                FileOutputStream fout = new FileOutputStream(userSettings);
                int readBytes;
                byte[] buffer = new byte[4096];
                while ((readBytes = in.read(buffer)) > 0) {
                    fout.write(buffer, 0, readBytes);
                }
                in.close();
                fout.close();
            }
            FileReader reader = new FileReader(userSettings);
            props.load(reader);
            BigBananaAppState.KEYBOARD_MOVE_UP = getKeyboardInput("keyboard.move.up", "W");
            BigBananaAppState.KEYBOARD_MOVE_DOWN = getKeyboardInput("keyboard.move.down", "S");
            BigBananaAppState.KEYBOARD_MOVE_RIGHT = getKeyboardInput("keyboard.move.right", "D");
            BigBananaAppState.KEYBOARD_MOVE_LEFT = getKeyboardInput("keyboard.move.left", "A");
            //InputBindings.PAUSE_KEY = getKeyboardInput("my.cool.action", "Space");

            reader.close();
        } catch (Exception e) {
            log.error("Error while loading user settings", e);
        }

        return props;
    }

    private static int getKeyboardInput(String key, String deflt) throws NoSuchFieldException, IllegalAccessException {
        String val = props.getProperty(key, deflt);
        for (int i = 0; i < 0xff; i++)
            if (KeyNames.getName(i).equals(val))
                return i;
        //return KeyInput.class.getField(props.getProperty(key, deflt)).getInt(null);
        return KeyInput.KEY_UNKNOWN;
    }

    @Override
    protected void initialize(Application app) {
        /*
        String gmPath[] = new String[]{};
        app.getAssetManager().registerLocator("assets", FileLocator.class);
        GoldMonkeyAppState gma = new GoldMonkeyAppState(false, COOKED_BUILD, "Normal", gmPath);
        if (mod != null) {
            gma.setBasePath("mods/GoldMonkey/");
        }
        getStateManager().attach(gma);
        */
        InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
        inputMapper.map(F_X_AXIS, InputState.Negative, PAD_MOVE_VERTICAL);
        inputMapper.map(F_X_AXIS, KEYBOARD_MOVE_RIGHT);
        inputMapper.map(F_X_AXIS, InputState.Negative, KEYBOARD_MOVE_LEFT);
        inputMapper.map(F_Y_AXIS, InputState.Negative, PAD_MOVE_HORIZONTAL);
        inputMapper.map(F_Y_AXIS, KEYBOARD_MOVE_UP);
        inputMapper.map(F_Y_AXIS, InputState.Negative, KEYBOARD_MOVE_DOWN);
        inputMapper.map(F_ACTIVATE, PAD_GREEN_A);
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

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        System.out.println("Pressed 'red b', 'cancel'");
    }

}
