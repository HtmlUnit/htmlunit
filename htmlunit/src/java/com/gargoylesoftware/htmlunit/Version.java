/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 * Class to display version information about HtmlUnit.  This is the class
 * that will be executed if the jar file is run.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Version {
    /**
     * The main entry point into this class.
     * @param args The arguments passed on the command line
     */
    public static void main( final String args[] ) {
        final Package aPackage = Package.getPackage("com.gargoylesoftware.htmlunit");

        System.out.println("HTMLUnit");
        System.out.println("Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.");

        if( aPackage != null ) {
            System.out.println("Version: "+aPackage.getImplementationVersion());
        }
    }
}
