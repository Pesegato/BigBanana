# BigBanana [![Build Status](https://travis-ci.org/Pesegato/BigBanana.svg?branch=master)](https://travis-ci.org/Pesegato/BigBanana) [![](https://jitpack.io/v/Pesegato/bigbanana.svg)](https://jitpack.io/#Pesegato/bigbanana)
Mouseless (bananaful?) navigation for jme3 applications

#### This flavor of the library interfaces directly with GLFW.

This library provide input utilities built on top of [Lemur](https://github.com/jMonkeyEngine-Contributions/Lemur), expecially useful when your game is played without the mouse and keyboard:
* proper Gamepad support (requires LWJGL3)
* configurable input bindings by means of a Property file (which can also be used for other user settings)
* remap input appstate
* 4-way navigation
* extra stuff
* sample app

## Proper Gamepad support

BigBanana uses the Gamepad input API from GLFW, to get input mapping right on supported Gamepads.

## Configurable input bindings

First, you must define the custom "actions" that will be bound to some input. Then you must add a keyboard mapping and/or a pad mapping.

    public static final String MY_COOL_ACTION = "my.cool.action";
    public static void main(String[] args) {
        BBBindings.addKeyMapping(MY_COOL_ACTION);
        BBBindings.addPadMapping(MY_COOL_ACTION);
        //No need to use setUseJoysticks!
        //settings.setUseJoysticks(true);

Then, you must attach the `BigBananaAppState`. The construction requires a `BananaPeel`, which is an interface for your application.

    String getFilePath(); //path of Properties file to save
    Properties getProperties(); //Properties must be loaded beforehand
    String getDefaultKeyBind(String key); //default key bindings for your application. Must include the custom actions.
    BBInput getDefaultPadBind(String key); //default pad bindings for your application. Must include the custom actions.

How do you implement the last method? See below for an example:

    @Override
    public BBInput getDefaultPadBind(String key) {
        switch (key) {
            case MY_COOL_ACTION:
                return BBInput.BB_BUTTON_X;
        }
        return null;
    }

Finally, on your application you can map ingame functions to the remapped actions.

     BigBananaAppState bbas = getState(BigBananaAppState.class);
     bbas.map(F_COOLACTION, MY_COOL_ACTION, this);
     bbas.mapBack(this);

Please note that you are mapping both keyboard and gamepad input, and therefore `this` must implement both `StateFunctionListener` and `AnalogFunctionListener`.

## Remap input appstate

Just attach the `RemapInputAppState` to show a user friendly gui that allows the user to remap all your custom action. You might want to change the visuals, but that is left as an exercise for the reader.

## 4-way navigation

Lemur already provides 2-way navigation and confirm and these are bound to the gui layout so you get them for free.
BigBanana instead allows you to build a logical layout (independent from the actual gui) where the component are placed on a virtual square table and take rectangular spaces.
You must add the `BBFocusTraversal` control to a spatial:

        BBFocusTraversal bbft = new BBFocusTraversal();
        myspatialonscreen.addControl(bbft);

Create a `Control` which implements `BBFocusTarget`. This is for getting visual feedback of the focus gained, lost etc.
Then you must add this `Control` to each focusable and finally you must add each focusable item to the virtual square:

        bbft.addFocusable(Spatial target, int row, int column, int width, int height)

Please note that adding a spatial to the virtual table do NOT automatically attach the spatial to a Node! You are simply adding navigation support.
Also don't forget to request focus for the first focusable item:

        bbft.receiveFocus();

When navigating between pages, you probably want to use multiple `BBFocusTraversal` control, one for each page and call `receiveFocus` when focused.

## Extra stuff

There are settings to changed the default deadzone, invert vertical axis, use the DPAD as left stick and the left stick as DPAD. There is a "navigation mode" for menus and a "gameplay mode" for playing. Have a look at the included example!

## Sample app

Please check the "sampleapp" folder, it contains an implementation of all the above.
