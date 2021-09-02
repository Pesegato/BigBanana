package com.pesegato.bigbanana;

import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class BBInput {

    public static final BBInput BB_BUTTON_A = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_A, "GamePad Button A");
    public static final BBInput BB_BUTTON_B = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_B,"GamePad Button B");
    public static final BBInput BB_BUTTON_X = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_X,"GamePad Button X");
    public static final BBInput BB_BUTTON_Y = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_Y,"GamePad Button Y");
    public static final BBInput BB_BUTTON_LBU = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER,"GamePad Button Left Bumper");
    public static final BBInput BB_BUTTON_RBU = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER,"GamePad Button Right Bumper");
    public static final BBInput BB_BUTTON_BACK = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_BACK,"GamePad Button Back");
    public static final BBInput BB_BUTTON_START = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_START,"GamePad Button Start");
    public static final BBInput BB_BUTTON_LTH = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB,"GamePad Button Left Thumb");
    public static final BBInput BB_BUTTON_RTH = new BBInput(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB,"GamePad Button Right Thumb");

    private int id;
    private String name;

    public BBInput(int id, String name) {
        this.id = id;
        this.name = name;
    }


    /**
     * Returns the logical ID of this button.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the human-readable name of this button.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        BBInput other = (BBInput) o;
        if (!Objects.equals(id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BBInput[" + id + "]";
    }
}
