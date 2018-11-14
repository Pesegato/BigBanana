# BigBanana [![Build Status](https://travis-ci.org/Pesegato/BigBanana.svg?branch=master)](https://travis-ci.org/Pesegato/BigBanana)
Mouseless (bananaful?) navigation for jme3 applications

This library provide input utilities built on top of [Lemur](https://github.com/jMonkeyEngine-Contributions/Lemur), expecially useful when your game is played without the mouse and keyboard:
* configurable input bindings by means of a Property file (which can also be used for other user settings)
* remap input appstate
* 4-way navigation
* extra stuff
* sample app

## Configurable input bindings

First, you must define the custom "actions" that will be bound to some input. Then you must add a keyboard mapping and/or a pad mapping.

    public static final String MY_COOL_ACTION = "my.cool.action";
    public static void main(String[] args) {
        BBBindings.addKeyMapping(MY_COOL_ACTION);
        BBBindings.addPadMapping(MY_COOL_ACTION);

Then, you must attach the `BigBananaAppState`. The construction requires a `BananaPeel`, which is an interface for your application.

    String getFilePath(); //path of Properties file to save
    Properties getProperties(); //Properties must be loaded beforehand
    String getDefaultKeyBind(String key); //default key bindings for your application. Must include the custom actions.
    String getDefaultPadBind(String key); //default pad bindings for your application. Must include the custom actions.

Finally, on your application you can map ingame functions to the remapped actions.

    inputMapper.map(F_COOLACTION, BBBindings.getK(MY_COOL_ACTION));

If you want to add the pad counterpart:

    inputMapper.map(F_COOLACTION, BBBindings.getP(MY_COOL_ACTION));

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

BigBananaFunctions.initializeDefaultMappings adds some useful pad input: start, back and select; grouped together.
When using these and switching between appstates, you should `addStateListener` and `removeStateListener` instead of `activateGroup` and `deactivateGroup`, otherwise the input event might be received by the wrong appstate!
There's also a StartGameAppState that provides a "Press START to play" functionality.

## Sample app

Please check the "sampleapp" folder, it contains an implementation of all the above.
