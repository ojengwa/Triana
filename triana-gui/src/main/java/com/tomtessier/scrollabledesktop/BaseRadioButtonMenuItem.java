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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 * This code is from a JavaWorld <a href="http://www.javaworld.com/javaworld/jw-11-2001/jw-1130-jscroll.html">
 * article</a> by Tom Tessier
 * <p/>
 * This class creates a base radio button menu item. ActionListener, mnemonic, keyboard shortcut, and title are set via
 * the constructor. <BR><BR> A {@link com.tomtessier.scrollabledesktop.BaseInternalFrame BaseInternalFrame} object may
 * optionally be associated with an instance of this class.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @version 1.0  11-Aug-2001
 */

public class BaseRadioButtonMenuItem extends JRadioButtonMenuItem
        implements FrameAccessorInterface {

    private BaseInternalFrame associatedFrame;


    /**
     * creates the BaseRadioButtonMenuItem with an associated frame. Used for radio menu items that are associated with
     * an internal frame.
     *
     * @param listener        the action listener to assign
     * @param itemTitle       the title of the item
     * @param mnemonic        the mnemonic used to access the menu
     * @param shortcut        the keyboard shortcut used to access the menu. -1 indicates no shortcut.
     * @param selected        <code>boolean</code> that indicates whether the menu item is selected or not
     * @param associatedFrame the BaseInternalFrame associated with the menu item
     */
    public BaseRadioButtonMenuItem(ActionListener listener,
                                   String itemTitle,
                                   int mnemonic,
                                   int shortcut,
                                   boolean selected,
                                   BaseInternalFrame associatedFrame) {

        this(listener, itemTitle, mnemonic, shortcut, selected);
        this.associatedFrame = associatedFrame;

    }

    /**
     * creates the BaseRadioButtonMenuItem without an associated frame. Used for generic radio button menu items.
     *
     * @param listener  the action listener to assign
     * @param itemTitle the title of the item
     * @param mnemonic  the mnemonic used to access the menu
     * @param shortcut  the keyboard shortcut used to access the menu. -1 indicates no shortcut.
     * @param selected  <code>boolean</code> that indicates whether the menu item is selected or not
     */
    public BaseRadioButtonMenuItem(ActionListener listener,
                                   String itemTitle,
                                   int mnemonic,
                                   int shortcut,
                                   boolean selected) {

        super(itemTitle, selected);
        setMnemonic(mnemonic);


        // set the alt-Shortcut accelerator
        if (shortcut != -1) {
            setAccelerator(
                    KeyStroke.getKeyStroke(
                            shortcut, ActionEvent.ALT_MASK));
        }

        setActionCommand(itemTitle + "Radio");
        addActionListener(listener);

    }


    /**
     * sets the associated frame
     *
     * @param associatedFrame the BaseInternalFrame object to associate with the menu item
     */
    public void setAssociatedFrame(BaseInternalFrame associatedFrame) {
        this.associatedFrame = associatedFrame;
    }

    /**
     * returns the associated frame
     *
     * @return the BaseInternalFrame object associated with this menu item
     */
    public BaseInternalFrame getAssociatedFrame() {
        return associatedFrame;
    }


}