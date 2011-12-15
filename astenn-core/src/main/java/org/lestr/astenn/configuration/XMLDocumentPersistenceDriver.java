/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author PIBONNIN
 */
public class XMLDocumentPersistenceDriver implements IPersistenceDriver {


    private Document document;


    public XMLDocumentPersistenceDriver() {

        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.appendChild(document.createElement("astenn"));
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLDocumentPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Constructor


    public XMLDocumentPersistenceDriver(Document document) {

        super();

        this.document = document;

    }// END Constructor


    /**
     * @return the document
     */
    public Document getDocument() {

        return document;

    }// END Method getDocument


    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {

        this.document = document;

    }// END Method setDocument


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        Element interfaceElement = getDocument().createElement("plugin-interface");
        interfaceElement.setAttribute("class-name", pluginInterfaceName);

        document.getDocumentElement().appendChild(interfaceElement);

    }// END Method addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        document.getDocumentElement().removeChild(getPluginInterfaceElement(pluginInterfaceName));

    }// END Method removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return getPluginInterfaceElement(pluginInterfaceName) != null;

    }// END Method existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        Element implementationElement = getDocument().createElement("plugin-implementation");
        implementationElement.setAttribute("address", pluginImplementationAddress);

        getPluginInterfaceElement(pluginInterfaceName).appendChild(implementationElement);

    }// END Method addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        Element pluginImplementationElement = getPluginImplementationElement(pluginInterfaceName, pluginImplementationAddress);
        pluginImplementationElement.getParentNode().removeChild(pluginImplementationElement);

    }// END Method removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        return existPluginInterface(pluginInterfaceName) && getPluginImplementationElement(pluginInterfaceName, pluginImplementationAddress) != null;

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        ArrayList<String> rslt = new ArrayList<String>();

        NodeList children = getDocument().getDocumentElement().getChildNodes();

        for (int i = 0; i <= children.getLength() - 1; i = i + 1) {

            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("plugin-interface")
                && ((Element) node).hasAttribute("class-name"))
                rslt.add(((Element) node).getAttribute("class-name"));

        }

        return rslt;

    }// END Method getPluginImplementationsNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        ArrayList<String> rslt = new ArrayList<String>();

        Element pluginInterfaceElement = getPluginInterfaceElement(pluginInterfaceName);

        if (pluginInterfaceElement != null) {

            NodeList children = pluginInterfaceElement.getChildNodes();

            for (int i = 0; i <= children.getLength() - 1; i = i + 1) {

                Node node = children.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals("plugin-implementation")
                    && ((Element) node).hasAttribute("address"))
                    rslt.add(((Element) node).getAttribute("address"));

            }

        }

        return rslt;

    }// END Method getPluginImplementationsStrings


    private Element getPluginInterfaceElement(String pluginInterfaceName) {

        NodeList children = getDocument().getDocumentElement().getChildNodes();

        for (int i = 0; i <= children.getLength() - 1; i = i + 1) {

            Node node = children.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE
                && node.getNodeName().equals("plugin-interface")
                && ((Element) node).hasAttribute("class-name")
                && ((Element) node).getAttribute("class-name").equals(pluginInterfaceName))
                return (Element) node;

        }

        return null;

    }// END Method getPluginInterfaceElement


    private Element getPluginImplementationElement(String pluginInterfaceName,
                                                   String pluginImplementationAddress) {

        Element pluginInterfaceElement = getPluginInterfaceElement(pluginInterfaceName);

        if (pluginInterfaceElement != null) {

            NodeList children = getDocument().getDocumentElement().getChildNodes();

            for (int i = 0; i <= children.getLength() - 1; i = i + 1) {

                Node node = children.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals("plugin-implementation")
                    && ((Element) node).hasAttribute("address")
                    && ((Element) node).getAttribute("address").equals(pluginImplementationAddress))
                    return (Element) node;

            }

        }

        return null;

    }// END Method getPluginInterfaceElement


}// END Class XMLDocumentPersistenceDriver


