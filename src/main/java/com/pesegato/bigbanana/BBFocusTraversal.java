/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.focus.FocusNavigationFunctions;
import com.simsilica.lemur.focus.FocusTraversal;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.simsilica.lemur.focus.FocusNavigationFunctions.F_ACTIVATE;

/**
 * @author Pesegato
 */
public class BBFocusTraversal extends AbstractControl implements FocusTraversal, StateFunctionListener {

    static Logger log = LoggerFactory.getLogger(BBFocusTraversal.class);

    static final int FOCUSMAP_LENGTH = 50;

    Spatial[][] focusMap = new Spatial[FOCUSMAP_LENGTH][FOCUSMAP_LENGTH];
    boolean[][] disableMap = new boolean[FOCUSMAP_LENGTH][FOCUSMAP_LENGTH];
    int focusPointerX = 0;
    int focusPointerY = 0;
    BBMouseListener bbml = new BBMouseListener();
    List<Spatial> spatialList = new ArrayList<>();

    public void setFocusMapSize(int width, int height) {
        focusMap = new Spatial[width][height];
    }

    public void addFocusable(Spatial target, int row, int column, int width, int height) {
        for (int i = row; i < row + height; i++) {
            for (int j = column; j < column + width; j++) {
                focusMap[i][j] = target;
            }
        }
        spatialList.add(target);
    }

    public void enableMouseListener() {
        GuiGlobals.getInstance().getInputMapper().addStateListener(this, FocusNavigationFunctions.F_ACTIVATE);
        for (Spatial s : spatialList) {
            MouseEventControl.addListenersToSpatial(s, bbml);
        }
    }

    public void disableMouseListener() {
        GuiGlobals.getInstance().getInputMapper().removeStateListener(this, FocusNavigationFunctions.F_ACTIVATE);
        for (Spatial s : spatialList) {
            MouseEventControl.removeListenersFromSpatial(s, bbml);
        }
    }

    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        log.trace("BBFT reveiced event");
        getCurrentFocus().getControl(BBFocusTarget.class).onValueChanged(func, value, tpf);
    }

    public void setEnabled(boolean enabled, int row, int column, int width, int height) {
        for (int i = row; i < row + height; i++) {
            for (int j = column; j < column + width; j++) {
                disableMap[i][j] = !enabled;
            }
        }
    }

    public void receiveFocus(boolean resetFocus) {
        if (resetFocus) {
            focusPointerX = 0;
            focusPointerY = 0;
        }
        GuiGlobals.getInstance().requestFocus(getCurrentFocus());
    }

    public void setCurrentFocus(Spatial s) {
        for (int i = 0; i < focusMap.length; i++) {
            for (int j = 0; j < focusMap[i].length; j++) {
                if (focusMap[i][j] == s) {
                    focusPointerX = j;
                    focusPointerY = i;
                    log.trace("focus now set on {} {}", focusPointerX, focusPointerY);
                    return;
                }
            }
        }
    }

    public Spatial getCurrentFocus() {
        return focusMap[focusPointerY][focusPointerX];
    }

    @Override
    public Spatial getDefaultFocus() {
        return focusMap[0][0];
    }

    @Override
    public Spatial getRelativeFocus(Spatial sptl, FocusTraversal.TraversalDirection td) {
        switch (td) {
            case Up:
                goUp(sptl);
                break;
            case Right:
                goRight(sptl);
                break;
            case Left:
                goLeft(sptl);
                break;
            case Down:
                goDown(sptl);
                break;
        }
        BigBananaAppState.log.trace("BB focus on {} {}", focusPointerX, focusPointerY);
        return focusMap[focusPointerY][focusPointerX];
    }

    void goUp(Spatial sptl) {
        Spatial s = null;
        int j = focusPointerY;
        while ((s == null) && (j > 0)) {
            j--;
            s = getValidFocusTarget(sptl, j, focusPointerX);
        }
        if ((s != null) && (sptl != s)) {
            focusPointerY = j;
            return;
        }
        BigBananaAppState.log.trace("BB nav best effort");
        //best effort
        for (int i = 0; i < focusMap.length; i++) {
            j = focusPointerY;
            while ((s == null) && (j > 0)) {
                j--;
                s = getValidFocusTarget(sptl, j, i);
            }
            if ((s != null) && (sptl != s)) {
                focusPointerX = i;
                focusPointerY = j;
                return;
            }
        }
    }

    void goRight(Spatial sptl) {
        Spatial s = null;
        int i = focusPointerX;
        while ((s == null) && (i < focusMap.length - 1)) {
            i++;
            s = getValidFocusTarget(sptl, focusPointerY, i);
        }
        if ((s != null) && (sptl != s)) {
            focusPointerX = i;
            return;
        }
        BigBananaAppState.log.trace("BB nav best effort");
        //best effort
        for (int j = 0; j < focusMap.length; j++) {
            i = focusPointerX;
            while ((s == null) && (i < focusMap.length - 1)) {
                i++;
                s = getValidFocusTarget(sptl, j, i);
            }
            if ((s != null) && (sptl != s)) {
                focusPointerX = i;
                focusPointerY = j;
                return;
            }
        }
    }

    void goLeft(Spatial sptl) {
        Spatial s = null;
        int i = focusPointerX;
        while ((s == null) && (i > 0)) {
            i--;
            s = getValidFocusTarget(sptl, focusPointerY, i);
        }
        if ((s != null) && (sptl != s)) {
            focusPointerX = i;
            return;
        }
        BigBananaAppState.log.trace("BB nav best effort");
        //best effort
        for (int j = 0; j < focusMap.length; j++) {
            i = focusPointerX;
            while ((s == null) && (i > 0)) {
                i--;
                s = getValidFocusTarget(sptl, j, i);
            }
            if ((s != null) && (sptl != s)) {
                focusPointerX = i;
                focusPointerY = j;
                return;
            }
        }
    }

    void goDown(Spatial sptl) {
        Spatial s = null;
        int j = focusPointerY;
        while ((s == null) && (j < focusMap.length - 1)) {
            j++;
            s = getValidFocusTarget(sptl, j, focusPointerX);
        }
        if ((s != null) && (sptl != s)) {
            focusPointerY = j;
            return;
        }
        BigBananaAppState.log.trace("BB nav best effort");
        //best effort
        for (int i = 0; i < focusMap.length; i++) {
            j = focusPointerY;
            while ((s == null) && (j < focusMap.length - 1)) {
                j++;
                s = getValidFocusTarget(sptl, j, i);
            }
            if ((s != null) && (sptl != s)) {
                focusPointerX = i;
                focusPointerY = j;
                return;
            }
        }
    }

    private Spatial getValidFocusTarget(Spatial old, int y, int x) {
        if (x < 0) {
            return null;
        }
        if (y < 0) {
            return null;
        }
        if (disableMap[y][x]) {
            return null;
        }
        Spatial s = focusMap[y][x];
        if (s == null) {
            return null;
        }
        if (s == old) {
            return null;
        }
        BBFocusTarget bbt = s.getControl(BBFocusTarget.class);
        if (bbt == null) {
            log.error("Component at {} {} don't have a BBFocusTarget Control", y, x);
            return null;
        }
        if (bbt.isFocusable()) {
            return s;
        }
        return null;
    }

    @Override
    public boolean isFocusRoot() {
        //not sure???
        return false;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    class BBMouseListener extends DefaultMouseListener {

        int prevX;
        int prevY;

        @Override
        public void mouseMoved(MouseMotionEvent event, Spatial target, Spatial capture) {
            int newX = event.getX();
            int newY = event.getY();
            if ((prevX == newX) && (prevY == newY)) {
                return;
            }
            log.trace("BBML moved {}", target);
            prevY = newY;
            prevX = newX;
            Spatial s = getCurrentFocus();
            log.trace("BBML current focus: {}", s);
            s.getControl(BBFocusTarget.class).focusLost();
            GuiGlobals.getInstance().requestFocus(target);
            BBFocusTarget bbft = getBBFocusTarget(target);
            bbft.focusGained();
            setCurrentFocus(target);
        }


        @Override
        public void mouseButtonEvent(MouseButtonEvent event, Spatial target, Spatial capture) {
            super.mouseButtonEvent(event, target, capture);
            if (event.isPressed()) {
                valueChanged(F_ACTIVATE, InputState.Positive, 0);
            } else if (event.isReleased()) {
                valueChanged(F_ACTIVATE, InputState.Off, 0);
            }
        }

        public <T extends BBFocusTarget> T getBBFocusTarget(Spatial s) {
            int numControls = s.getNumControls();
            for (int i = 0; i < numControls; i++) {
                Control c = s.getControl(i);
                if (BBFocusTarget.class.isAssignableFrom(c.getClass())) {
                    return (T) c;
                }
            }
            return null;
        }
    }
}
