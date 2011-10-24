/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.example.simple;

import org.lestr.astenn.PluginsManager;

/**
 *
 * @author pibonnin
 */
public class Main {


    public static void main(String... args){
        
        install();
        run();
        uninstall();

    }// END Method main


    public static void install(){

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, APlugin.class);
        PluginsManager.getSingleton().registerPlugin(IPlugin.class, BPlugin.class);

    }// END Method install


    public static void run(){

        for(IPlugin myPlugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class))
            System.out.println(myPlugin.getMessage());

    }// END Method run


    public static void uninstall(){

        PluginsManager.getSingleton().unregisterPlugin(IPlugin.class, BPlugin.class);
        PluginsManager.getSingleton().unregisterPlugin(IPlugin.class, APlugin.class);

    }// END Method uninstall


}// END Class Main
