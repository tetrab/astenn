/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.plugin;


/**
 *
 * @author PIBONNIN
 */
public interface IPersistenceDriver {


    public void addPluginInterface(String pluginInterfaceName);


    public void removePluginInterface(String pluginInterfaceName);


    public boolean existPluginInterface(String pluginInterfaceName);


    public void addPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress);


    public void removePluginImplementation(String pluginInterfaceName, String pluginImplementationAddress);


    public boolean  existPluginImplementation(String pluginInterfaceName, String pluginImplementationAddress);


    public Iterable<String> getPluginInterfacesNames();


    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName);

    
}
