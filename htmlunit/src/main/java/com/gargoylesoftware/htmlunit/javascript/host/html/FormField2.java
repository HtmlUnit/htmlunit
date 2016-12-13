package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;

public class FormField2 extends HTMLElement2 {

    /**
     * Returns the value of the JavaScript attribute {@code value}.
     *
     * @return the value of this attribute
     */
    @Getter
    public String getValue() {
        return getDomNodeOrDie().getAttribute("value");
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    @Setter
    public void setValue(final Object newValue) {
        getDomNodeOrDie().setAttribute("value", newValue.toString());
    }

}
