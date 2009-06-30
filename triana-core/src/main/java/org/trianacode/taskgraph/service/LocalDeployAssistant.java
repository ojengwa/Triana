/*
 * Copyright 2004 - 2009 University of Cardiff.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.trianacode.taskgraph.service;

import org.trianacode.taskgraph.TaskGraph;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 * @created Jun 25, 2009: 1:04:09 PM
 * @date $Date:$ modified by $Author:$
 */
public interface LocalDeployAssistant {

    /**
     * Handle the local publish (and view if required) of the specified taskgraph
     */
    public void localDeploy(TaskGraph taskgraph, TrianaClient client);

    /**
     * Handle the local retract of the specified taskgraph
     */
    public void localRetract(TaskGraph taskgraph);

}