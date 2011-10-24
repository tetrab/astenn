/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.plugin.IPluginsProvider;
import java.util.HashMap;
import java.util.Map;

public class RMIPluginsProvider implements IPluginsProvider {


    private Map<Class<?>, Object> proxies;


    public RMIPluginsProvider() {

        proxies = new HashMap<Class<?>, Object>();

    }// END Constructor


    @Override
    public String getScheme() {

        return "rmi";

    }// END Method getScheme


    @Override
    public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType,
                                                               String pluginImplementationAddress) {

        if (!proxies.containsValue(pluginInterfaceType))
            try {
                proxies.put(pluginInterfaceType, Naming.lookup("//" + pluginImplementationAddress.substring(getScheme().length() + 1)));
            } catch (RemoteException ex) {
                Logger.getLogger(RMIPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(RMIPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(RMIPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            }

        return (PluginInterfaceType) proxies.get(pluginInterfaceType);

    }// END Method getPlugin


}// END Class ARSPluginsManager


