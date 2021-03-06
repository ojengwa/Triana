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

package org.trianacode.taskgraph.ser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Description Here...
 *
 * @author Andrew Harrison
 * @version $Revision:$
 */

public class DocumentHandler {

    private static TransformerFactory tf = TransformerFactory.newInstance();

//    static {
//        tf.setAttribute("indent-number", new Integer(2));
//    }

    private Document doc;

    public DocumentHandler() throws IOException {
        this.doc = newDocument();
        if (doc == null) {
            throw new IOException("could not create a new document!");
        }

    }

    public DocumentHandler(InputStream in) throws IOException {
        this.doc = newDocument(in);
        if (doc == null) {
            throw new IOException("could not create a new document!");
        }
    }

    public DocumentHandler(Reader in) throws IOException {
        this.doc = newDocument(in);
        if (doc == null) {
            throw new IOException("could not create a new document!");
        }
    }

    public Element element(String name) {
        return doc.createElement(name);
    }

    public Element element(String namespace, String name) {
        return doc.createElementNS(namespace, name);
    }

    public Element root() {
        return doc.getDocumentElement();
    }

    public boolean hasChildren(Element parent) {
        NodeList ch = parent.getChildNodes();
        if (ch != null) {
            if (ch.getLength() > 0) {
                return true;
            }
        }
        return false;
    }

    public Element add(Object o, Element parent) {
        if (o instanceof Element) {
            parent.appendChild((Element) o);
        } else if (o instanceof String) {
            parent.setTextContent((String) o);
        }
        return parent;
    }

    public Element setRoot(Element o) {
        doc.appendChild(o);
        return o;
    }

    public Document document() {
        return doc;
    }


    public void addAttribute(Element e, String key, String value) {
        e.setAttribute(key, value);
    }

    public void output(OutputStream out, boolean pretty) throws IOException {
        if (pretty) {
            prettify(doc, out);
        } else {
            transform(doc, out);
        }
    }

    public void output(OutputStream out) throws IOException {
        transform(doc, out);
    }

    public void output(Writer out, boolean pretty) throws IOException {
        if (pretty) {
            prettify(doc, out);
        } else {
            transform(doc, out);
        }
    }

    public Element getChild(Element parent, String tag) {
        NodeList ch = parent.getChildNodes();
        if (ch != null) {
            int len = ch.getLength();
            for (int i = 0; i < len; i++) {
                Node n = ch.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    if (e.getLocalName().equals(tag)) {
                        return e;
                    }
                }
            }
        }
        return null;

    }

    public List<Element> getChildren(Element parent, String tag) {
        List<Element> childers = new ArrayList<Element>();
        NodeList ch = parent.getChildNodes();
        if (ch != null) {
            int len = ch.getLength();
            for (int i = 0; i < len; i++) {
                Node n = ch.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    if (e.getLocalName().equals(tag)) {
                        childers.add(e);
                    }
                }
            }
        }
        return childers;
    }

    public List<Element> getChildren(Element parent) {
        List<Element> childers = new ArrayList<Element>();
        NodeList ch = parent.getChildNodes();
        if (ch != null) {
            int len = ch.getLength();
            for (int i = 0; i < len; i++) {
                Node n = ch.item(i);
                if (n instanceof Element) {
                    Element e = (Element) n;
                    childers.add(e);
                }
            }
        }
        return childers;
    }


    private static Document newDocument() {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static Document newDocument(InputStream stream) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static Document newDocument(Reader in) {
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(in));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static final String prettyPrintStylesheet =
            "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0' " +
                    " xmlns:xalan='http://xml.apache.org/xslt' " +
                    " exclude-result-prefixes='xalan'>" +
                    "  <xsl:output method='xml' indent='yes' xalan:indent-amount='4'/>" +
                    "  <xsl:strip-space elements='*'/>" +
                    "  <xsl:template match='/'>" +
                    "    <xsl:apply-templates/>" +
                    "  </xsl:template>" +
                    "  <xsl:template match='node() | @*'>" +
                    "        <xsl:copy>" +
                    "          <xsl:apply-templates select='node() | @*'/>" +
                    "        </xsl:copy>" +
                    "  </xsl:template>" +
                    "</xsl:stylesheet>";

    public void prettify(Node element, OutputStream out) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transform(element, baos);

        Source stylesheetSource = new StreamSource(new ByteArrayInputStream(prettyPrintStylesheet.getBytes()));
        Source xmlSource = new StreamSource(new ByteArrayInputStream(baos.toByteArray()));

        try {
            Templates templates = tf.newTemplates(stylesheetSource);
            Transformer transformer = templates.newTransformer();
            transformer.transform(xmlSource, new StreamResult(out));
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

    public void prettify(Node element, Writer out) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transform(element, baos);

        Source stylesheetSource = new StreamSource(new ByteArrayInputStream(prettyPrintStylesheet.getBytes()));
        Source xmlSource = new StreamSource(new ByteArrayInputStream(baos.toByteArray()));

        try {
            Templates templates = tf.newTemplates(stylesheetSource);
            Transformer transformer = templates.newTransformer();
            transformer.transform(xmlSource, new StreamResult(out));
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }


    private StreamResult transform(Node doc, Writer out) throws IOException {
        Transformer t = null;
        try {
            t = tf.newTransformer();

            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(out);
        try {
            t.transform(doms, sr);

        } catch (TransformerException te) {
            throw new IOException(te.getMessage());
        }
        return sr;
    }

    private StreamResult transform(Node doc, OutputStream out) throws IOException {

        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        } catch (TransformerConfigurationException tce) {
            assert (false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(out);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            throw new IOException(te.getMessage());
        }
        return sr;
    }


}
