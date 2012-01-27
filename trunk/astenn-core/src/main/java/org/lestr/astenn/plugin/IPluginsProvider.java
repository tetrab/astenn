/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.plugin;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.PluginsManager;

/**
 *
 * @author PIBONNIN
 */
public interface IPluginsProvider {


    public String getScheme();


    public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType,
                                                               String pluginImplementationAddress);


    public static class LocalPluginsProvider implements IPluginsProvider {


        @Override
        public String getScheme() {

            return "local";

        }// END Method getScheme


        @Override
        public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType,
                                                                   String pluginImplementationAddress) {

            PluginInterfaceType rslt = null;

            try {

                String implementationClassName = pluginImplementationAddress.substring((getScheme() + ":").length());
                Class<?> implementationClass = getClass().getClassLoader().loadClass(implementationClassName);

                Object instance = implementationClass.newInstance();

                if (pluginInterfaceType.isInstance(instance))
                    rslt = (PluginInterfaceType) instance;
                else
                    for (IEquivalentPluginInterfaceAdapter adapter : PluginsManager.getSingleton().getRegisteredLocalPlugins(IEquivalentPluginInterfaceAdapter.class))
                        if (adapter.getPluginInterface() == pluginInterfaceType
                            && adapter.getPluginEquivalentInterface().isInstance(instance))
                            rslt = (PluginInterfaceType) adapter.adaptPluginInstance(instance);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }

            return rslt;

        }// END Method getPlugin


    }// END Class LocalPluginsProvider


}// END Interface IPluginsProvider
