package Chreator;

import Chreator.UIModule.UIHandler;

/**
 * Created by him on 2015/12/2.
 */
public class ChreatorLauncher {
    private UIHandler ui;

    public static void main (String[] args){new ChreatorLauncher();}

    public ChreatorLauncher(){
        ui = UIHandler.getInstance(getUIEventCallback());
    }

    private UIHandler.EventCallback getUIEventCallback(){
        return new UIHandler.EventCallback() {
        };
    }
}
