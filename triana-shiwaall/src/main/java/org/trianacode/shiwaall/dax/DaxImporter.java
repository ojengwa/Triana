package org.trianacode.shiwaall.dax;

import org.trianacode.config.TrianaProperties;
import org.trianacode.gui.extensions.TaskGraphImporterInterface;
import org.trianacode.taskgraph.TaskGraph;
import org.trianacode.taskgraph.TaskGraphException;

import java.io.File;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: Aug 31, 2010
 * Time: 11:04:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class DaxImporter implements TaskGraphImporterInterface {

    /* (non-Javadoc)
     * @see org.trianacode.gui.extensions.TaskGraphImporterInterface#importWorkflow(java.io.File, org.trianacode.config.TrianaProperties)
     */
    @Override
    public TaskGraph importWorkflow(File file, TrianaProperties properties) throws TaskGraphException, IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
