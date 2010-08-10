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

package org.trianacode.gui.components.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import org.trianacode.gui.hci.color.ColorManager;
import org.trianacode.gui.main.NodeComponent;
import org.trianacode.gui.main.TrianaLayoutConstants;
import org.trianacode.taskgraph.Node;

/**
 * A semi circular node
 *
 * @author Ian Wang
 * @version $Revision: 4048 $
 */

public class CircleNode extends AbstractButton implements NodeComponent {

    // the node this component represents
    private Node node;

    // a flag indicating whether this node can be connected to
    private boolean connectable;


    /**
     * Constructs a connectable semi-circular node
     */
    public CircleNode(Node node) {
        this(node, true);
    }

    /**
     * Constructs a semi-circular node
     */
    public CircleNode(Node node, boolean connectable) {
        this.node = node;
        this.connectable = connectable;

        setPreferredSize(new Dimension(TrianaLayoutConstants.DEFAULT_NODE_SIZE.width,
                TrianaLayoutConstants.DEFAULT_NODE_SIZE.height));
    }

    /**
     * @return this component
     */
    public Component getComponent() {
        return this;
    }

    /**
     * @return the interface to the node this component represents
     */
    public Node getNode() {
        return node;
    }


    /**
     * @return true if the node represented by this component can be connected to/connected from
     */
    public boolean isConnectable() {
        return connectable;
    }

    /**
     * @return true if the node represented by this component is an input node
     */
    public boolean isInputNode() {
        return node.isInputNode();
    }

    /**
     * @return true if the node represented by this component is an output node
     */
    public boolean isOutputNode() {
        return node.isOutputNode();
    }


    protected void paintComponent(Graphics graphs) {
        Dimension size = getSize();
        Color gcol = graphs.getColor();

        graphs.setColor(ColorManager.getColor(node));
        graphs.fillOval(0, 0, size.width, size.height);
        graphs.setColor(gcol);
    }

}
