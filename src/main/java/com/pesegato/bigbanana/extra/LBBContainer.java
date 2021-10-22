package com.pesegato.bigbanana.extra;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.pesegato.bigbanana.BBFocusTraversal;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;

public class LBBContainer extends Node {
    Container c = new Container();
    BBFocusTraversal bbft = new BBFocusTraversal();

    public LBBContainer() {
        this.attachChild(c);
        addControl(bbft);
    }

    public <T extends Node> T addChild(T child, Object... constraints) {
        return c.addChild(child, constraints);
    }

    public <T extends Node> T addFocusableChild(T child, int row, int column, int width, int height, Object... constraints) {
        bbft.addFocusable(child, row, column, width, height);
        return c.addChild(child, constraints);
    }

    public <T extends Label> T addLemurChild(T child, int row, int column, int width, int height, Object... constraints) {
        bbft.addFocusable(child, row, column, width, height);
        child.addControl(new LBBFocusTarget());
        return c.addChild(child, constraints);
    }

    /**
     * Call this when child is nested inside another Container
     *
     * @param child
     * @param row
     * @param column
     * @param width
     * @param height
     * @param <T>
     * @return
     */

    public <T extends Label> T addFocusable(T child, int row, int column, int width, int height) {
        bbft.addFocusable(child, row, column, width, height);
        child.addControl(new LBBFocusTarget());
        return child;
    }

    public Vector3f getPreferredSize() {
        return c.getPreferredSize();
    }

    public void focus() {
        GuiGlobals.getInstance().requestFocus(c);
    }

    public void setPreferredSize(Vector3f vector3f) {
        c.setPreferredSize(vector3f);
    }
}
