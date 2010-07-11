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

package org.trianacode.taskgraph.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.trianacode.taskgraph.tool.creators.type.ClassHierarchy;
import org.trianacode.taskgraph.tool.creators.type.ClassParser;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */

public class TypesMap {

    private static Map<String, ClassHierarchy> allByName = new ConcurrentHashMap<String, ClassHierarchy>();
    private static Map<String, Map<String, ClassHierarchy>> allByType
            = new ConcurrentHashMap<String, Map<String, ClassHierarchy>>();
    private static Map<String, ClassHierarchy> annotated = new ConcurrentHashMap<String, ClassHierarchy>();
    private static ClassParser parser = new ClassParser();
    private static Set<String> deadEnds = new HashSet<String>();

    public static void load(File file) throws IOException {
        Map<String, ClassHierarchy> hiers = parser.readFile(file);
        allByName.putAll(hiers);
        for (String s : hiers.keySet()) {
            ClassHierarchy ch = hiers.get(s);
            if (ch.isAnnotated()) {
                annotated.put(ch.getFile(), ch);
            }
        }
    }

    /**
     * get a class hierarchy based on the full path to the class under scrutiny (will be the full path to a file in the
     * case of of a non-zipped file, or a jar: URL pointing to an entry if jarred), and the type to match against, e.g.
     * a getClass().getName() type string.
     *
     * @param path
     * @param type
     * @return
     */
    public static ClassHierarchy isType(String path, String type) {
        Map<String, ClassHierarchy> mapped = allByType.get(type);
        if (mapped != null && mapped.get(path) != null) {
            return mapped.get(path);
        }
        for (String hier : allByName.keySet()) {
            if (isType(allByName, hier, type)) {
                ClassHierarchy ch = allByName.get(hier);
                addToTypes(type, ch);
                if (ch.getFile().equals(path)) {
                    return ch;
                }
            }
        }
        return null;
    }

    private static void addToTypes(String type, ClassHierarchy ch) {
        Map<String, ClassHierarchy> types = allByType.get(type);
        if (types == null) {
            types = new HashMap<String, ClassHierarchy>();
        }
        types.put(ch.getFile(), ch);
        allByType.put(type, types);

    }


    public static List<ClassHierarchy> find(String type) throws IOException {
        long now = System.currentTimeMillis();
        Set<ClassHierarchy> ret = new HashSet<ClassHierarchy>();
        for (String hier : allByName.keySet()) {
            if (isType(allByName, hier, type)) {
                ret.add(allByName.get(hier));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("TypeMap.find time:" + (end - now));
        return new ArrayList<ClassHierarchy>(ret);
    }

    public static ClassHierarchy getAnnotated(String path) throws IOException {
        return annotated.get(path);
    }


    private static boolean isType(Map<String, ClassHierarchy> hiers, String hier, String type) {
        ClassHierarchy ch = hiers.get(hier);
        if (ch == null || deadEnds.contains(hier)) {
            return false;
        }
        if (!ch.isPublic() || !ch.isConcrete()) {

            return false;
        }
        String[] intfs = ch.getInterfaces();
        for (String intf : intfs) {
            if (intf.equals(type)) {
                return true;
            }
        }
        for (String intf : intfs) {
            boolean is = isType(hiers, intf, type);
            if (is) {
                return true;
            } else {
                deadEnds.add(intf);
            }
        }

        String superClass = ch.getSuperClass();
        if (superClass.equals(type)) {
            return true;
        } else {
            boolean b = isType(hiers, superClass, type);
            if (!b) {
                deadEnds.add(hier);
            }
            return b;
        }
    }


    private static String convert(String type) {
        if (type.endsWith(".class")) {
            type = type.substring(0, type.length() - 6);
        }
        type = type.replace(File.separator, ".");
        type = type.replace("/", ".");
        return type;

    }


}
