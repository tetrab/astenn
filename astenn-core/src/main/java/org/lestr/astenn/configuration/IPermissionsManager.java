/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.configuration;

import java.util.Collection;

/**
 *
 * @author PIBONNIN
 */
public interface IPermissionsManager {


    Collection<IPermissionsManagerListener> getListeners();


    void setAutoExposeLocalPlugins(boolean autoExpose);


    boolean isAutoExposeLocalPlugins();


    void rescanAutoExposedLocalPlugins();


    void exposeLocalPlugin(Class<?> localPluginInterface,
                           Class<?> localPluginImplementation);


    void unexposeLocalPlugin(Class<?> localPluginInterface,
                             Class<?> localPluginImplementation);


    Iterable<Class<?>[]> getExposedLocalPlugins();


    public static interface IPermissionsManagerListener {


        void localPluginExposed(Class<?> localPluginInterface,
                                Class<?> localPluginImplementation);


        void localPluginUnexposing(Class<?> localPluginInterface,
                                   Class<?> localPluginImplementation);


    }// END Interface IPermissionsManagerListener


}// END Interface IPermissionsManager


