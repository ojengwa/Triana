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

package org.trianacode.gui.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.trianacode.gui.Display;

/**
 * Methods of accessing the preferred sizing of triana components
 *
 * @author Ian Wang
 * @version $Revision: 4048 $
 */

public interface TrianaLayoutConstants {

    /**
     * a Triana unit is the preferred height of a tool and is used as the base for other dimensions
     */
    public static double TRIANA_UNIT = Display.screenY / 20;

    /**
     * the default size of a tool
     */
    public static Dimension DEFAULT_TOOL_SIZE = new Dimension((int) (TRIANA_UNIT * 3), (int) TRIANA_UNIT);

    /**
     * the default size of a node
     */
    public static Dimension DEFAULT_NODE_SIZE = new Dimension((int) (TRIANA_UNIT * 0.22), (int) (TRIANA_UNIT * 0.2));

    /**
     * the default size of a plus minus icon
     */
    public static Dimension DEFAULT_PLUS_MINUS_SIZE = new Dimension((int) (TRIANA_UNIT * 0.15),
            (int) (TRIANA_UNIT * 0.15));

    /**
     * the default font size
     */
    public static Font DEFAULT_FONT = new Font("Sans-Serif", Font.PLAIN, (int) (TRIANA_UNIT * 0.3));

    public static Font SMALL_FONT = new Font("Sans-Serif", Font.PLAIN, (int) (TRIANA_UNIT * 0.2));

    /**
     * a node's default connected color
     */
    public static Color DEFAULT_CONNECTED_NODE_COLOR = Color.black;

    /**
     * a node's default unconnected color
     */
    public static Color DEFAULT_UNCONNECTED_NODE_COLOR = new Color(75, 75, 75);

    /**
     * the default color used to denote a node as a trigger node
     */
    public static Color DEFAULT_TRIGGER_NODE_COLOR = new Color(200, 0, 0);

    /**
     * the default color used for the diamond that denotes parameter nodes
     */
    public static Color DEFAULT_PARAMETER_NODE_COLOR = Color.white;

}
