package com.pesegato.bigbanana.sampleapp;


import com.jme3.app.*;
import com.jme3.input.KeyInput;
import com.jme3.renderer.RenderManager;
import com.pesegato.bigbanana.*;
import com.simsilica.lemur.input.*;
import com.simsilica.lemur.GuiGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class Main extends SimpleApplication {

    static Logger log = LoggerFactory.getLogger( Main.class );
    public static boolean COOKED_BUILD;

    public static void main(String[] args){
        Main app = new Main();
        app.start(); // start the game
    }

    public Main() {
        super(new StatsAppState());
        COOKED_BUILD = false;//new File("assets").exists();
    }

    @Override
    public void simpleInitApp() {

        // Initialize Lemur subsystems and setup the default
        // camera controls.
        GuiGlobals.initialize(this);
        getProperties("BigBanana","test");

        BigBananaAppState bbas=new BigBananaAppState();
        BananizedAppState ban=new BananizedAppState();
        bbas.setReceiver(ban);
        stateManager.attachAll(bbas,ban);

    }
    public static Properties props = new Properties();
    public static String defPath;

    private static int getKeyboardInput(String key, String deflt) throws NoSuchFieldException, IllegalAccessException {
        return KeyInput.class.getField(props.getProperty(key, deflt)).getInt(null);
    }

    private static Button getButtonInput(String key, String deflt) throws NoSuchFieldException, IllegalAccessException {
        return (Button) Button.class.getField(props.getProperty(key, deflt)).get(null);
    }


    public static Properties getProperties(String folderName, String fileName){
        defPath="~/."+folderName+"/";
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                defPath = System.getenv("USERPROFILE") + "/Saved Games/"+folderName+"/";
            }
            File userSettings = new File(defPath + fileName+".properties");
            File f = new File(defPath);
            if (!userSettings.exists()) {
                f.mkdir();
                InputStream in = Main.class.getResourceAsStream(COOKED_BUILD?"":"/" + fileName + ".properties");
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

            InputBindings.OK_KEY = getKeyboardInput("keyboard.ok", "KEY_RETURN");
            InputBindings.CANCEL_KEY = getKeyboardInput("keyboard.cancel", "KEY_BACK");
            InputBindings.MOVEUP_K = getKeyboardInput("keyboard.move.up", "KEY_W");
            InputBindings.MOVEDN_K = getKeyboardInput("keyboard.move.down", "KEY_S");
            InputBindings.MOVERI_K = getKeyboardInput("keyboard.move.right", "KEY_D");
            InputBindings.MOVELE_K = getKeyboardInput("keyboard.move.left", "KEY_A");
            //InputBindings.PAUSE_KEY = getKeyboardInput("keyboard.pause", "KEY_P");

            InputBindings.OK_PAD = getButtonInput("pad.shoot", "JOYSTICK_BUTTON1");
            InputBindings.CANCEL_PAD = getButtonInput("pad.kick", "JOYSTICK_BUTTON2");
            //InputBindings.PAUSE_PAD = getButtonInput("pad.pause", "JOYSTICK_BUTTON6");
            InputBindings.MOVEV_PAD = (Axis) Axis.class.getField(props.getProperty("pad.movev", "JOYSTICK_LEFT_Y")).get(null);
            InputBindings.MOVEH_PAD = (Axis) Axis.class.getField(props.getProperty("pad.moveh", "JOYSTICK_LEFT_X")).get(null);

            reader.close();
        } catch (IOException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            // file does not exist
            log.error("Error while loading user settings", ex);
        }
        return props;
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
