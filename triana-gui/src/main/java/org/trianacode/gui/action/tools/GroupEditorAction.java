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

package org.trianacode.gui.action.tools;

import org.trianacode.gui.Display;
import org.trianacode.gui.action.ActionDisplayOptions;
import org.trianacode.gui.action.ToolSelectionHandler;
import org.trianacode.gui.hci.GUIEnv;
import org.trianacode.gui.hci.MenuMnemonics;
import org.trianacode.gui.panels.GroupEditor;
import org.trianacode.gui.util.Env;
import org.trianacode.gui.windows.ParameterWindow;
import org.trianacode.gui.windows.WindowButtonConstants;
import org.trianacode.taskgraph.TaskGraph;
import org.trianacode.taskgraph.tool.ToolTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * The action for showing the node editor
 *
 * @author Ian Wang
 * @version $Revision: 4048 $
 */

public class GroupEditorAction extends AbstractAction implements ActionDisplayOptions {

    private ToolSelectionHandler selhandler;
    private ToolTable tooltable;

    public GroupEditorAction(ToolSelectionHandler sel, ToolTable tooltable, int displayOption, JMenu parentMenu) {
        this(sel, tooltable, displayOption);
        char mnem = MenuMnemonics.getInstance().getNextMnemonic(parentMenu, Env.getString("GroupEditor"));
        putValue(MNEMONIC_KEY, new Integer(mnem));
    }

    public GroupEditorAction(ToolSelectionHandler sel, ToolTable tooltable, int displayOption) {
        super();
        this.selhandler = sel;
        this.tooltable = tooltable;
        putValue(SHORT_DESCRIPTION, Env.getString("GroupEditorTip"));
        putValue(ACTION_COMMAND_KEY, Env.getString("GroupEditor"));
        if ((displayOption == DISPLAY_ICON) || (displayOption == DISPLAY_BOTH)) {
            putValue(SMALL_ICON, GUIEnv.getIcon("unitproperties.png"));
        }
        if ((displayOption == DISPLAY_NAME) || (displayOption == DISPLAY_BOTH)) {
            putValue(NAME, Env.getString("GroupEditor") + "...");
        }
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (selhandler.getSelectedTaskgraph() != null) {
            showGroupEditorFor(selhandler.getSelectedTaskgraph(), e.getSource());
        }
    }


    /**
     * Show the node editor for the specified task.
     *
     * @param source the source object asking for the node editor to be shown
     */
    protected void showGroupEditorFor(TaskGraph taskgraph, Object source) {
        ParameterWindow groupEditorWindow = new ParameterWindow(GUIEnv.getApplicationFrame(),
                WindowButtonConstants.OK_CANCEL_APPLY_BUTTONS, true);
        groupEditorWindow.setTitle(taskgraph.getToolName() + ": " + Env.getString("GroupEditor"));

        GroupEditor groupEditor = new GroupEditor(taskgraph, tooltable);
        groupEditor.setTask(taskgraph);
        groupEditor.init();

        groupEditorWindow.setParameterPanel(groupEditor);

        Display.clipFrameToScreen(groupEditorWindow);

        groupEditorWindow.requestFocus();

        Point loc = Display.getAnchorPoint(source, groupEditorWindow);
        loc.translate(140, 40);

        groupEditorWindow.setLocation(loc);
        Display.clipFrameToScreen(groupEditorWindow);

        groupEditorWindow.setVisible(true);
        groupEditorWindow.requestFocus();
    }


}
