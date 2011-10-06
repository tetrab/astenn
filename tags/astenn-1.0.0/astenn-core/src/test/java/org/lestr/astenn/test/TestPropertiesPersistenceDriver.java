/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.test;

import org.lestr.astenn.plugin.IPersistenceDriver;
import junit.framework.TestCase;
import org.lestr.astenn.configuration.PropertiesPersistenceDriver;

/**
 *
 * @author PIBONNIN
 */
public class TestPropertiesPersistenceDriver extends TestCase {


    protected IPersistenceDriver persistenceDriver;


    @Override
    protected void setUp() throws Exception {

        persistenceDriver = new PropertiesPersistenceDriver();

    }// END Method setUp


    public void test1() {

        assertFalse(persistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertFalse(persistenceDriver.existPluginInterface("test"));

        persistenceDriver.addPluginInterface("test");

        assertTrue(persistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertFalse(persistenceDriver.existPluginImplementation("test", "local:test"));

        persistenceDriver.addPluginImplementation("test", "local:test");

        assertTrue(persistenceDriver.getPluginImplementationsAddresses("test").iterator().hasNext());
        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertTrue(persistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(persistenceDriver.existPluginImplementation("test", "local:test2"));

        persistenceDriver.removePluginImplementation("test", "local:test");

        assertTrue(persistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertFalse(persistenceDriver.getPluginImplementationsAddresses("test").iterator().hasNext());
        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertFalse(persistenceDriver.existPluginImplementation("test", "local:test"));

        persistenceDriver.removePluginInterface("test");

        assertFalse(persistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertFalse(persistenceDriver.existPluginInterface("test"));

    }// END Method test1


}// END Class TestPluginsManager

