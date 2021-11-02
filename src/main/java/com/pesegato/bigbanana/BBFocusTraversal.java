/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pesegato.bigbanana;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.focus.FocusTraversal;

/**
 * @author Pesegato
 */
public class BBFocusTraversal extends AbstractControl implements FocusTraversal {

    static final int FOCUSMAP_LENGTH = 50;

    Spatial[][] focusMap = new Spatial[FOCUSMAP_LENGTH][FOCUSMAP_LENGTH];
    boolean[][] disableMap = new boolean[FOCUSMAP_LENGTH][FOCUSMAP_LENGTH];
    int focusPointerX = 0;
    int focusPointerY = 0;

    public void setFocusMapSize(int width, int height) {
        focusMap = new Spatial[width][height];
    }

    public void addFocusable(Spatial target, int row, int column, int width, int height) {
        for (int i = row; i < row + height; i++) {
            for (int j = column; j < column + width; j++) {
                focusMap[i][j] = target;
            }
        }
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
            for (int j = 0; j < focusMap[i].length; i++) {
                if (focusMap[i][j] == s) {
                    focusPointerX = j;
                    focusPointerY = i;
                    System.out.println("focus now set on " + focusPointerX + " " + focusPointerY);
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
                focusPointerY = goUp(sptl, focusPointerY);
                break;
            case Right:
                focusPointerX = goRight(sptl, focusPointerX);
                break;
            case Left:
                focusPointerX = goLeft(sptl, focusPointerX);
                break;
            case Down:
                focusPointerY = goDown(sptl, focusPointerY);
                break;
        }
        System.out.println("focus on " + focusPointerX + " " + focusPointerY);
        return focusMap[focusPointerY][focusPointerX];
    }

    int goUp(Spatial sptl, int index) {
        boolean foundEnabled = false;
        while (!foundEnabled) {
            if (index == 0) {
                return 0;
            }
            if (disableMap[index - 1][focusPointerX]) {
                index--;
            } else foundEnabled = true;
        }
        Spatial s = focusMap[index - 1][focusPointerX];
        if (s == null) {
            return focusPointerY;
        }
        BBFocusTarget bbt = s.getControl(BBFocusTarget.class);
        if (bbt == null) {
            System.out.println("Component at " + (index - 1) + " " + focusPointerX + " don't have a BBFocusTarget Control");
            return focusPointerY;
        }
        if (bbt.isFocusable()
                && sptl != s) {
            return index - 1;
        }
        return goUp(sptl, index - 1);

    }

    int goRight(Spatial sptl, int index) {
        boolean foundEnabled = false;
        while (!foundEnabled) {
            if (index > focusMap.length) {
                return focusPointerX;
            }
            if (disableMap[focusPointerY][index + 1]) {
                index++;
            } else foundEnabled = true;
        }
        Spatial s = focusMap[focusPointerY][index + 1];
        if (s == null) {
            return focusPointerX;
        }
        BBFocusTarget bbt = s.getControl(BBFocusTarget.class);
        if (bbt == null) {
            System.out.println("Component at " + focusPointerY + " " + (index + 1) + " don't have a BBFocusTarget Control");
            return focusPointerY;
        }
        if (bbt.isFocusable()
                && sptl != s) {
            return index + 1;
        }
        return goRight(sptl, index + 1);

    }

    int goLeft(Spatial sptl, int index) {
        boolean foundEnabled = false;
        while (!foundEnabled) {
            if (index == 0) {
                return 0;
            }
            if (disableMap[focusPointerY][index - 1]) {
                index--;
            } else foundEnabled = true;
        }
        Spatial s = focusMap[focusPointerY][index - 1];
        if (s == null) {
            return focusPointerX;
        }
        BBFocusTarget bbt = s.getControl(BBFocusTarget.class);
        if (bbt == null) {
            System.out.println("Component at " + focusPointerY + " " + (index - 1) + " don't have a BBFocusTarget Control");
            return focusPointerY;
        }
        if (bbt.isFocusable()
                && sptl != s) {
            return index - 1;
        }
        return goLeft(sptl, index - 1);

    }

    int goDown(Spatial sptl, int index) {
        boolean foundEnabled = false;
        while (!foundEnabled) {
            if (index > focusMap.length) {
                return focusPointerX;
            }
            if (disableMap[index + 1][focusPointerX]) {
                index++;
            } else foundEnabled = true;
        }
        Spatial s = focusMap[index + 1][focusPointerX];
        if (s == null) {
            return focusPointerY;
        }
        BBFocusTarget bbt = s.getControl(BBFocusTarget.class);
        if (bbt == null) {
            System.out.println("Component at " + (index + 1) + " " + focusPointerX + " don't have a BBFocusTarget Control");
            return focusPointerY;
        }
        if (bbt.isFocusable()
                && sptl != s) {
            return index + 1;
        }
        return goDown(sptl, index + 1);

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

}
