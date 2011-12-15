/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.test;

import org.lestr.astenn.configuration.JavaPreferencesPersistenceDriver;

/**
 *
 * @author PIBONNIN
 */
public class TestXMLDocumentPersistenceDriver extends TestRAMPersistenceDriver {


    @Override
    protected void setUp() throws Exception {

        persistenceDriver = new JavaPreferencesPersistenceDriver("test");

    }// END Method setUp
    

}// END Class TestPluginsManager
