package org.trianacode.gui.main.organize;

import org.apache.commons.logging.Log;
import org.trianacode.enactment.logging.Loggers;
import org.trianacode.taskgraph.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Ian Harvey
 * Date: Nov 25, 2010
 * Time: 9:14:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class DaxUnitObject {

    private DaxLevel level;
    private int row;
    private Task task;

    private DaxUnitObject() {
    }

    public DaxUnitObject(Task t) {
        setTask(t);
    }

    public void setLevel(DaxLevel level) {
        this.level = level;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public DaxLevel getLevel() {
        return level;
    }

    public int getRow() {
        return row;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public String toString() {
        return getTask().getToolName();
    }

    public void leaveLevel() {
        getLevel().removeDUO(this);
    }

    public void setParams() {
        Task t = getTask();
        String levelValue = String.valueOf(getLevel().getLevelNumber() * 2.3);
        String rowValue = String.valueOf(getRow() * 1.5);

        log("Setting value to level: " + levelValue + " row: " + rowValue);

        t.setParameter(Task.GUI_X, levelValue);
        t.setParameter(Task.GUI_Y, rowValue);
    }

    private void log(String text) {
        Log log = Loggers.DEV_LOGGER;
        log.debug(text);
        //      System.out.println(text);
    }
}
