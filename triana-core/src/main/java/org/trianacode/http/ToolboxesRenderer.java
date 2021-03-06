package org.trianacode.http;

import org.thinginitself.streamable.Streamable;
import org.trianacode.taskgraph.tool.Toolbox;
import org.trianacode.taskgraph.tool.ToolboxTree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrew Harrison
 * @version 1.0.0 Jul 20, 2010
 */

public class ToolboxesRenderer implements Renderer {


    private List<Toolbox> toolboxes;
    private String path;

    public void init(List<Toolbox> toolboxes, String path) {
        this.toolboxes = toolboxes;
        this.path = path;

    }

    public Streamable render(String type, String mime) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("path", path);
        ToolboxTree tree = createToolboxTree(toolboxes);
        String ss = treesToString(tree, new HtmlListSerializer());
        properties.put("toolboxes", ss);
        return Output.output(properties, type, mime);
    }

    protected ToolboxTree createToolboxTree(List<Toolbox> toolboxes) {
        ToolboxTree tree = new ToolboxTree();
        for (int i = 0; i < toolboxes.size(); i++) {
            Toolbox toolbox = toolboxes.get(i);
            tree.addToolbox(toolbox);
        }
        return tree;
    }

    protected String treesToString(ToolboxTree tree, TreeSerializer ser) {
        Iterator<ToolboxTree.TreeNode> it = tree.iterator();
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        ToolboxTree.TreeNode last = null;
        sb.append(ser.begin());
        while (it.hasNext()) {

            ToolboxTree.TreeNode node = it.next();
            int newDepth = node.getDepth();
            int diff = newDepth - depth;
            sb.append(space(newDepth));
            if (diff < 0) {
                if (last != null && last.isLeaf()) {
                    sb.append(ser.endLeaf());
                    sb.append(space(newDepth));
                }
                for (int i = diff; i < -1; i++) {
                    sb.append(ser.endBranch(false));
                    sb.append(space(newDepth - i));
                }
                sb.append(ser.endBranch(newDepth == 1));
                sb.append(space(newDepth));
            } else if (diff == 0) {
                if (last != null) {
                    if (!last.isLeaf()) {
                        sb.append(ser.endBranch(newDepth == 1));
                    } else {
                        sb.append(ser.endLeaf());
                    }
                }
            }
            sb.append(space(newDepth));
            if (last == null) {
                sb.append(ser.startRoot(node));
            } else {
                sb.append(ser.startNode(node));
            }
            depth = newDepth;
            last = node;
        }
        sb.append(ser.endLeaf());
        for (int i = depth; i > 2; i--) {
            sb.append(space(i));
            sb.append(ser.endBranch(false));
        }
        sb.append(space(1));
        sb.append(ser.endBranch(true));
        sb.append(ser.endRoot());
        sb.append(ser.end());
        String ret = sb.toString();
        return ret;
    }

    private String space(int depth) {
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }


}
