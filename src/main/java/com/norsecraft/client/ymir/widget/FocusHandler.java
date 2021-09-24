package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;

public class FocusHandler {

    public static void cycleFocus(GuiInterpretation host, boolean lookForwards) {
        boolean result;
        YmirWidget focus = host.getFocus();
        if(focus == null)
            result = cycleFocus(host, lookForwards, host.getRootPanel(), null);
        else
            result = cycleFocus(host, lookForwards, focus, null);
        if(!result)
            cycleFocus(host, lookForwards, host.getRootPanel(), null);
    }

    private static boolean cycleFocus(GuiInterpretation host, boolean lookForwards, YmirWidget widget, YmirWidget pivot) {
        YmirWidget next = widget instanceof YmirPanel ? ((YmirPanel) widget).cycleFocus(lookForwards, pivot) : widget.cycleFocus(lookForwards);

        if(next != null) {
            host.requestFocus(next);
        } else {
            YmirPanel parent = widget.getParent();
            if(parent != null) {
                return cycleFocus(host, lookForwards, parent, widget);
            }
        }
        return false;
    }

}
