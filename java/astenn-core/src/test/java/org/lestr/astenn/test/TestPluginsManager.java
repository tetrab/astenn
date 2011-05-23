/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.test;

import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.configuration.RAMPersistenceDriver;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 *
 * @author PIBONNIN
 */
public class TestPluginsManager extends TestCase {


    @Override
    protected void setUp() throws Exception {

        PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().setReadWritePersistenceDriver(new RAMPersistenceDriver());
        PluginsManager.getSingleton().getConfiguration().getPersistenceDriver().getReadOnlyPersistenceDrivers().clear();
        
    }// END Method setUp


    public void test1()
    {

        assertFalse(PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class).iterator().hasNext());

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, Plugin1.class);

        assertTrue(PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class).iterator().hasNext());

        PluginsManager.getSingleton().unregisterPlugin(IPlugin.class, Plugin1.class);

        assertFalse(PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class).iterator().hasNext());

    }// END Method test1


    public void test2()
    {

        assertNull(PluginsManager.getSingleton().getRegisteredSingletonPlugin(IPlugin.class));

        PluginsManager.getSingleton().registerSingletonPlugin(IPlugin.class, Plugin1.class);

        assertNotNull(PluginsManager.getSingleton().getRegisteredSingletonPlugin(IPlugin.class));

        PluginsManager.getSingleton().unregisterSingletonPlugin(IPlugin.class);

        assertNull(PluginsManager.getSingleton().getRegisteredSingletonPlugin(IPlugin.class));

    }// END Method test2


    public void test3()
    {

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, Plugin1.class);
        PluginsManager.getSingleton().registerPlugin(IPlugin.class, Plugin2.class);

        Iterator<IPlugin> enumerator = PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class).iterator();
        assertTrue(enumerator.hasNext()); enumerator.next();
        assertTrue(enumerator.hasNext()); enumerator.next();
        assertFalse(enumerator.hasNext());

    }// END Method test3


    public void test4()
    {

        PluginsManager.getSingleton().registerSingletonPlugin(IPlugin.class, Plugin1.class);

        assertTrue(PluginsManager.getSingleton().getRegisteredSingletonPlugin(IPlugin.class).getClass() == Plugin1.class);

        PluginsManager.getSingleton().registerSingletonPlugin(IPlugin.class, Plugin2.class);

        assertTrue(PluginsManager.getSingleton().getRegisteredSingletonPlugin(IPlugin.class).getClass() == Plugin2.class);

    }// END Method test4


    public static interface IPlugin
    {

        public boolean test1();

    }// END Interface IPlugin


    public static class Plugin1 implements IPlugin
    {

        @Override
        public boolean test1(){ return true; }

    }// END Class Plugin1


    public static class Plugin2 implements IPlugin
    {

        @Override
        public boolean test1(){ return true; }

    }// END Class Plugin2
    

}// END Class TestPluginsManager
