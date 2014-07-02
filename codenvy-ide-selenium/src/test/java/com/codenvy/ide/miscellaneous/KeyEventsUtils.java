package com.codenvy.ide.miscellaneous;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codenvy on 19.02.14.
 */
public class KeyEventsUtils {
    public List<Integer> keyEvents = new ArrayList<Integer>();

    List<Integer> getKeys() {
        keyEvents.add(KeyEvent.VK_BACK_QUOTE);
        keyEvents.add(KeyEvent.VK_0);
        keyEvents.add(KeyEvent.VK_1);
        keyEvents.add(KeyEvent.VK_2);
        keyEvents.add(KeyEvent.VK_3);
        keyEvents.add(KeyEvent.VK_4);
        keyEvents.add(KeyEvent.VK_5);
        keyEvents.add(KeyEvent.VK_6);
        keyEvents.add(KeyEvent.VK_7);
        keyEvents.add(KeyEvent.VK_8);
        keyEvents.add(KeyEvent.VK_9);
        keyEvents.add(KeyEvent.VK_MINUS);
        keyEvents.add(KeyEvent.VK_EQUALS);
        keyEvents.add(KeyEvent.VK_Q);
        keyEvents.add(KeyEvent.VK_W);
        keyEvents.add(KeyEvent.VK_E);
        keyEvents.add(KeyEvent.VK_R);
        keyEvents.add(KeyEvent.VK_T);
        keyEvents.add(KeyEvent.VK_Y);
        keyEvents.add(KeyEvent.VK_U);
        keyEvents.add(KeyEvent.VK_I);
        keyEvents.add(KeyEvent.VK_O);
        keyEvents.add(KeyEvent.VK_P);
        keyEvents.add(KeyEvent.VK_OPEN_BRACKET);
        keyEvents.add(KeyEvent.VK_CLOSE_BRACKET);
        keyEvents.add(KeyEvent.VK_A);
        keyEvents.add(KeyEvent.VK_S);
        keyEvents.add(KeyEvent.VK_D);
        keyEvents.add(KeyEvent.VK_F);
        keyEvents.add(KeyEvent.VK_G);
        keyEvents.add(KeyEvent.VK_H);
        keyEvents.add(KeyEvent.VK_J);
        keyEvents.add(KeyEvent.VK_K);
        keyEvents.add(KeyEvent.VK_L);
        keyEvents.add(KeyEvent.VK_SEMICOLON);
        keyEvents.add(KeyEvent.VK_QUOTE);
        keyEvents.add(KeyEvent.VK_Z);
        keyEvents.add(KeyEvent.VK_X);
        keyEvents.add(KeyEvent.VK_C);
        keyEvents.add(KeyEvent.VK_V);
        keyEvents.add(KeyEvent.VK_B);
        keyEvents.add(KeyEvent.VK_N);
        keyEvents.add(KeyEvent.VK_M);
        keyEvents.add(KeyEvent.VK_COMMA);
        keyEvents.add(KeyEvent.VK_PERIOD);
        keyEvents.add(KeyEvent.VK_SLASH);
        keyEvents.add(KeyEvent.VK_BACK_SLASH);
        return keyEvents;
    }
}
