/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author pibonnin
 */
public class EmbbedXMLDocumentPersistenceDriver implements IPersistenceDriver {


    @Override
    public void addPluginInterface(String pluginInterfaceName) { }


    @Override
    public void removePluginInterface(String pluginInterfaceName) { }


    @Override
    public void addPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) { }


    @Override
    public void removePluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) { }


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return readEmbbedXMLDocuments().existPluginInterface(pluginInterfaceName);

    }// END Method existPluginInterface


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress) {
        
        return readEmbbedXMLDocuments().existPluginImplementation(pluginInterfaceName, pluginImplementationAddress);
        
    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        return readEmbbedXMLDocuments().getPluginInterfacesNames();

    }// END Method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        return readEmbbedXMLDocuments().getPluginImplementationsAddresses(pluginInterfaceName);

    }// END Method getPluginImplementationsStrings


    private CompositePersistenceDriver readEmbbedXMLDocuments(){

        CompositePersistenceDriver rslt = new CompositePersistenceDriver();

        try {

            Enumeration<URL> enumeration = this.getClass().getClassLoader().getResources("META-INF/astenn.xml");

            while(enumeration.hasMoreElements())
                try {
                    rslt.getReadOnlyPersistenceDrivers().add(new XMLDocumentPersistenceDriver(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(enumeration.nextElement().openStream())));
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(EmbbedXMLDocumentPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SAXException ex) {
                    Logger.getLogger(EmbbedXMLDocumentPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EmbbedXMLDocumentPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
                }

        } catch (IOException ex) {
            Logger.getLogger(EmbbedXMLDocumentPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method readEmbbedXMLDocuments


}// END Class EmbbedXMLDocumentPersistenceDriver
