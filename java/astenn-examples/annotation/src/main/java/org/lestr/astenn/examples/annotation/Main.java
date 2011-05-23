/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.annotation;

import org.lestr.astenn.PluginsManager;

/**
 *
 * @author pibonnin
 */
public class Main {


    public static void main(String... args){

        for(IPlugin myPlugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class))
            System.out.println(myPlugin.getMessage());

    }// END Method main


}// END Class Main
