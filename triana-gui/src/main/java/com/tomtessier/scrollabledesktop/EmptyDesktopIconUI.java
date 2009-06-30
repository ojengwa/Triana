/*
 * The University of Wales, Cardiff Triana Project Software License (Based
 * on the Apache Software License Version 1.1)
 *
 * Copyright (c) 2007 University of Wales, Cardiff. All rights reserved.
 *
 * Redistribution and use of the software in source and binary forms, with
 * or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * 2.  Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any,
 *    must include the following acknowledgment: "This product includes
 *    software developed by the University of Wales, Cardiff for the Triana
 *    Project (http://www.trianacode.org)." Alternately, this
 *    acknowledgment may appear in the software itself, if and wherever
 *    such third-party acknowledgments normally appear.
 *
 * 4. The names "Triana" and "University of Wales, Cardiff" must not be
 *    used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact triana@trianacode.org.
 *
 * 5. Products derived from this software may not be called "Triana," nor
 *    may Triana appear in their name, without prior written permission of
 *    the University of Wales, Cardiff.
 *
 * 6. This software may not be sold, used or incorporated into any product
 *    for sale to third parties.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN
 * NO EVENT SHALL UNIVERSITY OF WALES, CARDIFF OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ------------------------------------------------------------------------
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Triana Project. For more information on the
 * Triana Project, please see. http://www.trianacode.org.
 *
 * This license is based on the BSD license as adopted by the Apache
 * Foundation and is governed by the laws of England and Wales.
 *
 */

package com.tomtessier.scrollabledesktop;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DesktopIconUI;
import java.awt.*;


/**
 * This code is from a JavaWorld <a href="http://www.javaworld.com/javaworld/jw-11-2001/jw-1130-jscroll.html">
 * article</a> by Tom Tessier
 *
 * This class provides an empty DesktopIconUI for
 * {@link com.tomtessier.scrollabledesktop.BaseDesktopPane BaseDesktopPane}.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  29-Jul-2001
 */

public class EmptyDesktopIconUI extends DesktopIconUI {

    /**
     * stores the instance of this class. Used by
     * {@link com.tomtessier.scrollabledesktop.EmptyDesktopIconUI#createUI(JComponent)
     * createUI}.
     */
    protected static EmptyDesktopIconUI desktopIconUI;


    /**
     * creates the EmptyDesktopIconUI object
     *
     * @param c the reference to the JComponent object required by createUI
     */
    public static ComponentUI createUI(JComponent c) {
        if (desktopIconUI == null) {
            desktopIconUI = new EmptyDesktopIconUI();
        }
        return desktopIconUI;
    }


    /**
     * overrides the paint method with a blank routine so that no
     * component is displayed when an internal frame is iconified
     *
     * @param g the reference to the Graphics object used to paint the desktop
     */
    protected void paint(Graphics g) {
    }

}
