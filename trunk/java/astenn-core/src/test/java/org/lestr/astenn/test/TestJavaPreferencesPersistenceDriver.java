/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.test;

import org.lestr.astenn.configuration.JavaPreferencesPersistenceDriver;
import java.util.prefs.Preferences;

/**
 *
 * @author PIBONNIN
 */
public class TestJavaPreferencesPersistenceDriver extends TestRAMPersistenceDriver {


    @Override
    protected void setUp() throws Exception {

        Preferences.userRoot().node("test").removeNode();
        persistenceDriver = new JavaPreferencesPersistenceDriver("test");

    }// END Method setUp


    @Override
    protected void tearDown() throws Exception {

        Preferences.userRoot().node("test").removeNode();

    }// END Method tearDown
    

}// END Class TestPluginsManager
