/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.protocol.aro;

import org.lestr.astenn.plugin.IPluginsProvider;

/**
 *
 * @author PIBONNIN
 */
public class AROPluginsProvider implements IPluginsProvider {


    public AROPluginsProvider()
    {

    }// END Constructor


    @Override
    public String getScheme() {

        return "aro";

    }// END Method getScheme


    @Override
    public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType, String pluginImplementationAddress) {

        return null;

    }// END Method getPlugin


}// END Class ARSPluginsManager
