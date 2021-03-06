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

package org.trianacode.taskgraph.imp;

import java.util.Hashtable;

import org.trianacode.taskgraph.Node;
import org.trianacode.taskgraph.Task;
import org.trianacode.taskgraph.TaskGraph;
import org.trianacode.taskgraph.event.ControlTaskStateEvent;
import org.trianacode.taskgraph.event.NodeEvent;
import org.trianacode.taskgraph.event.NodeListener;
import org.trianacode.taskgraph.event.ParameterUpdateEvent;
import org.trianacode.taskgraph.event.TaskDisposedEvent;
import org.trianacode.taskgraph.event.TaskGraphCableEvent;
import org.trianacode.taskgraph.event.TaskGraphListener;
import org.trianacode.taskgraph.event.TaskGraphTaskEvent;
import org.trianacode.taskgraph.event.TaskListener;
import org.trianacode.taskgraph.event.TaskNodeEvent;
import org.trianacode.taskgraph.event.TaskPropertyEvent;

/**
 * A class that is responsible for maintaining the integrity of a control loop, with regards to the correct number of
 * group/loop input and output nodes.
 *
 * @author Ian Wang
 * @version $Revision: 4048 $
 */

public class ControlLoopChecker implements TaskGraphListener, TaskListener, NodeListener {

    private Task control;
    private TaskGraph parent;

    private Hashtable nodetable = new Hashtable();
    private boolean stable = true;


    public ControlLoopChecker(Task control) {
        this.control = control;
        this.parent = control.getParent();

        this.control.addTaskListener(this);
        this.parent.addTaskGraphListener(this);

        updateNodes();

        Node[] nodes = control.getDataInputNodes();
        for (int count = 0; count < nodes.length; count++) {
            nodes[count].addNodeListener(this);
        }

        nodes = control.getDataOutputNodes();
        for (int count = 0; count < nodes.length; count++) {
            nodes[count].addNodeListener(this);
        }

    }


    private void updateNodes() {
        nodetable.clear();

        Node[] innodes = control.getDataInputNodes();
        Node[] outnodes = control.getDataOutputNodes();
        int groupin = control.getParent().getDataInputNodeCount();
        int groupout = control.getParent().getDataOutputNodeCount();

        for (int count = 0; count < innodes.length; count++) {
            if (count < groupin) {
                nodetable.put(innodes[count], outnodes[groupout + count]);
                nodetable.put(outnodes[groupout + count], innodes[count]);
            } else {
                nodetable.put(innodes[count], outnodes[count - groupin]);
                nodetable.put(outnodes[count - groupin], innodes[count]);
            }
        }
    }


    /**
     * Called when the control task is connected/disconnected or unstable
     */
    public void controlTaskStateChanged(ControlTaskStateEvent event) {
        if (event.getControlTaskState() == TaskGraph.CONTROL_TASK_CONNECTED) {
            updateNodes();
            stable = true;
        } else if (event.getControlTaskState() == TaskGraph.CONTROL_TASK_UNSTABLE) {
            stable = false;
        } else if (event.getControlTaskState() == TaskGraph.CONTROL_TASK_DISCONNECTED) {
            stable = true;
        }
    }

    /**
     * Called when a data input node is added.
     */
    public void nodeAdded(TaskNodeEvent event) {
        if (event.getTask() == control) {
            event.getNode().addNodeListener(this);
        }
    }

    /**
     * Called when a data input node is removed.
     */
    public void nodeRemoved(TaskNodeEvent event) {
        if (event.getTask() == control) {
            Node node = event.getNode();
            node.removeNodeListener(this);

            if ((stable) && (nodetable.containsKey(node))) {
                Node oppnode = (Node) nodetable.get(node);

                int oldstate = parent.getControlTaskState();
                parent.setControlTaskState(TaskGraph.CONTROL_TASK_UNSTABLE);

                nodetable.remove(oppnode);
                control.removeNode(oppnode);

                parent.setControlTaskState(oldstate);
            }
        }
    }

    /**
     * Called before a node is diconnected from a cable.
     */
    public void nodeDisconnected(NodeEvent event) {
        if ((event.getNode().getTask() == control) && stable) {
            Node node = event.getNode();

            if (node.isBottomLevelNode() && (nodetable.containsKey(node))) {
                Node oppnode = (Node) nodetable.get(node);

                int oldstate = parent.getControlTaskState();
                parent.setControlTaskState(TaskGraph.CONTROL_TASK_UNSTABLE);

                nodetable.remove(node);
                nodetable.remove(oppnode);
                control.removeNode(node);
                control.removeNode(oppnode);

                parent.setControlTaskState(oldstate);
            }
        }
    }

    /**
     * Called before the task is disposed
     */
    public void taskDisposed(TaskDisposedEvent event) {
        control.removeTaskListener(this);
    }


    /**
     * Called when a new task is created in a taskgraph.
     */
    public void taskCreated(TaskGraphTaskEvent event) {
    }

    /**
     * Called when a task is removed from a taskgraph. Note that this method is called when tasks are removed from a
     * taskgraph due to being grouped (they are placed in the new groups taskgraph).
     */
    public void taskRemoved(TaskGraphTaskEvent event) {
    }

    /**
     * Called when a new connection is made between two tasks.
     */
    public void cableConnected(TaskGraphCableEvent event) {
    }

    /**
     * Called before a connection between two tasks is removed.
     */
    public void cableDisconnected(TaskGraphCableEvent event) {
    }

    /**
     * Called when a connection is reconnected to a different task.
     */
    public void cableReconnected(TaskGraphCableEvent event) {
    }

    /**
     * Called when a node is connected to a cable.
     */
    public void nodeConnected(NodeEvent event) {
    }

    /**
     * Called when one of a group node's parents changes
     */
    public void nodeParentChanged(NodeEvent event) {
    }

    /**
     * Called when one of a group node's child changes
     */
    public void nodeChildChanged(NodeEvent event) {
    }

    /**
     * Called when the name of the parameter the node is inputting/outputting is set.
     */
    public void parameterNameSet(NodeEvent event) {
    }


    /**
     * Called when the value of a parameter is changed, including when a parameter is removed.
     */
    public void parameterUpdated(ParameterUpdateEvent event) {
    }

    /**
     * Called when the core properties of a task change i.e. its name, whether it is running continuously etc.
     */
    public void taskPropertyUpdate(TaskPropertyEvent event) {
    }

}
