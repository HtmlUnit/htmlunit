/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

public class Version {
    public static void main( final String args[] ) {
        final Package aPackage = Package.getPackage("com.gargoylesoftware.htmlunit");

        System.out.println("HTMLUnit");
        System.out.println("Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.");

        if( aPackage != null ) {
            System.out.println("Version: "+aPackage.getImplementationVersion());
        }
    }
}
