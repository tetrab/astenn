/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.cxf.test;

import javax.jws.WebMethod;
import javax.jws.WebService;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.cxf.SOAPServer;
import junit.framework.TestCase;

/**
 *
 * @author PIBONNIN
 */
public class TestSOAP extends TestCase {


    private SOAPServer server;


    @Override
    protected void setUp() throws Exception {

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, Plugin.class);
        PluginsManager.getSingleton().getConfiguration().getPermissionsManager().exposeLocalPlugin(IPlugin.class, Plugin.class);

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, "soap:http://127.0.0.1:8078/" + IPlugin.class.getName() + "/" + Plugin.class.getName());

        server = new SOAPServer();
        server.start();

    }// END Method setUp


    @Override
    protected void tearDown() throws Exception {

        server.stop();

    }// END Method tearDown


    public void test1() {

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class))
            assertTrue(plugin.test1());

    }// END Method test1


    public void test2() {

        Bean beanE = new Bean();
        beanE.setI(1);
        beanE.s = 1;
        beanE.l = 1;
        beanE.b = 1;
        beanE.setBool(true);
        beanE.str = "str";
        beanE.iTab = new int[]{1};
        beanE.beanTab = new Bean[]{new Bean()};

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class)) {

            Bean beanB = plugin.test2(beanE);

            assertEquals(beanB.getI(), 1);
            assertEquals(beanB.s, 1);
            assertEquals(beanB.l, 1);
            assertEquals(beanB.b, 1);
            assertTrue(beanB.isBool());
            assertEquals(beanB.str, "str");
            assertEquals(beanB.iTab.length, 1);
            assertEquals(beanB.iTab[0], 1);
            assertEquals(beanB.beanTab.length, 1);

        }

    }// END Method test2


    public void test3() {

        for (IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class))
            try {

                plugin.test3();
                fail();

            } catch (RuntimeException e) {
                assertEquals(e.getMessage(), "e");
            }

    }// END Method test1


    @WebService
    public static interface IPlugin {

        
        @WebMethod
        public boolean test1();


        @WebMethod
        public Bean test2(Bean bean);


        @WebMethod
        public void test3();


    }// END Interface IPlugin


    public static class Plugin implements IPlugin {


        @Override
        public boolean test1() {
            return true;
        }


        @Override
        public Bean test2(Bean bean) {
            return bean;
        }


        @Override
        public void test3() {
            throw new RuntimeException("e");
        }


    }// END Class Plugin


    public static class Bean {


        private int i;


        public long l;


        public short s;


        public byte b;


        private boolean bool;


        public String str;


        public int[] iTab;


        public Bean[] beanTab;


        public int getI() {
            return i;
        }


        public void setI(int i) {
            this.i = i;
        }


        public boolean isBool() {
            return bool;
        }


        public void setBool(boolean bool) {
            this.bool = bool;
        }


    }// END Class Bean


}// END Class TestPluginsManager


