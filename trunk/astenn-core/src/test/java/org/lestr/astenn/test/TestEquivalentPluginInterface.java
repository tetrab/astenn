/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.test;

import junit.framework.TestCase;
import org.lestr.astenn.PluginsManager;

/**
 *
 * @author PIBONNIN
 */
public class TestEquivalentPluginInterface extends TestCase {


    public void test1() {

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, Plugin1.class);
        PluginsManager.getSingleton().registerPlugin(IFrenchPlugin.class, Plugin2.class);

        int i = 0;

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class)) {
            assertTrue(plugin.test());
            i = i + 1;
        }

        assertEquals(1, i);

        PluginsManager.getSingleton().getAdvanced().registerEquivalentPluginInterface(IPlugin.class, IFrenchPlugin.class, Translator.class);

        i = 0;

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class)) {
            assertTrue(plugin.test());
            i = i + 1;
        }

        assertEquals(2, i);

        PluginsManager.getSingleton().getAdvanced().unregisterEquivalentPluginInterface(IPlugin.class, IFrenchPlugin.class, Translator.class);

        i = 0;

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class)) {
            assertTrue(plugin.test());
            i = i + 1;
        }

        assertEquals(1, i);

    }// END Method test1


    public static interface IPlugin {


        public boolean test();


    }// END Interface IPlugin


    public static interface IFrenchPlugin {


        public boolean frenchTest();


    }// END Interface IPlugin


    public static class Translator implements IPlugin {


        private IFrenchPlugin frenchPlugin;


        public Translator(IFrenchPlugin frenchPlugin) {

            this.frenchPlugin = frenchPlugin;

        }// END Constructor


        @Override
        public boolean test() {

            return frenchPlugin.frenchTest();

        }// END Méthode test


    }// END Class Plugin1


    public static class Plugin1 implements IPlugin {


        @Override
        public boolean test() {

            return true;

        }// END Méthode test


    }// END Class Plugin1


    public static class Plugin2 implements IFrenchPlugin {


        @Override
        public boolean frenchTest() {

            return true;

        }// END Méthode frenchTest


    }// END Class Plugin2


}// END Class TestPluginsManager


