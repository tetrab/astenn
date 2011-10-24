/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.test;

import org.lestr.astenn.configuration.CompositePersistenceDriver;
import org.lestr.astenn.plugin.IPersistenceDriver;
import org.lestr.astenn.configuration.RAMPersistenceDriver;
import junit.framework.TestCase;

/**
 *
 * @author PIBONNIN
 */
public class TestCompositePersistenceDriver extends TestCase {


    protected IPersistenceDriver persistenceDriver;
    protected IPersistenceDriver writeReadPersistenceDriver;
    protected IPersistenceDriver readPersistenceDriver;


    @Override
    protected void setUp() throws Exception {

        writeReadPersistenceDriver = new RAMPersistenceDriver();
        readPersistenceDriver = new RAMPersistenceDriver();
        persistenceDriver = new CompositePersistenceDriver(writeReadPersistenceDriver, readPersistenceDriver);

    }// END Method setUp


    public void test1() {

        persistenceDriver.addPluginInterface("test");

        assertTrue(persistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertTrue(writeReadPersistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertTrue(writeReadPersistenceDriver.existPluginInterface("test"));
        assertFalse(readPersistenceDriver.getPluginInterfacesNames().iterator().hasNext());
        assertFalse(readPersistenceDriver.existPluginInterface("test"));

        persistenceDriver.addPluginImplementation("test", "local:test");

        assertTrue(persistenceDriver.getPluginImplementationsAddresses("test").iterator().hasNext());
        assertTrue(persistenceDriver.existPluginImplementation("test", "local:test"));
        assertTrue(writeReadPersistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(readPersistenceDriver.existPluginImplementation("test", "local:test"));

        persistenceDriver.removePluginImplementation("test", "local:test");

        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertFalse(persistenceDriver.existPluginImplementation("test", "local:test"));
        assertTrue(writeReadPersistenceDriver.existPluginInterface("test"));
        assertFalse(writeReadPersistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(readPersistenceDriver.existPluginInterface("test"));
        assertFalse(readPersistenceDriver.existPluginImplementation("test", "local:test"));

        persistenceDriver.removePluginInterface("test");

        assertFalse(persistenceDriver.existPluginInterface("test"));
        assertFalse(writeReadPersistenceDriver.existPluginInterface("test"));
        assertFalse(readPersistenceDriver.existPluginInterface("test"));

        readPersistenceDriver.addPluginInterface("test");
        readPersistenceDriver.addPluginImplementation("test", "local:test");

        assertTrue(persistenceDriver.existPluginInterface("test"));
        assertFalse(writeReadPersistenceDriver.existPluginInterface("test"));
        assertTrue(readPersistenceDriver.existPluginInterface("test"));
        assertTrue(persistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(writeReadPersistenceDriver.existPluginImplementation("test", "local:test"));
        assertTrue(readPersistenceDriver.existPluginImplementation("test", "local:test"));

        readPersistenceDriver.removePluginImplementation("test", "local:test");

        assertFalse(persistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(writeReadPersistenceDriver.existPluginImplementation("test", "local:test"));
        assertFalse(readPersistenceDriver.existPluginImplementation("test", "local:test"));
        
        readPersistenceDriver.removePluginInterface("test");

        assertFalse(persistenceDriver.existPluginInterface("test"));
        assertFalse(writeReadPersistenceDriver.existPluginInterface("test"));
        assertFalse(readPersistenceDriver.existPluginInterface("test"));

    }// END Method test1


}// END Class TestCompositePersistenceDriver

