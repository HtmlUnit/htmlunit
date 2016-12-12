package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.js.nashorn.internal.objects.Global;

public interface GlobalAction {

    public Object run(Global global);
}
