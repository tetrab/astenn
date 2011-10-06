/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.plugin;

/**
 *
 * @author pibonnin
 */
public interface IServer {


    String getName();


    void start() throws Exception;


    void stop() throws Exception;


    String getPluginRemoteAddress(Class<?> pluginInterfaceClass, Class<?> pluginImplementationClass);
    

}// END Interface IServer
