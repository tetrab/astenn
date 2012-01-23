/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn;

import org.lestr.astenn.configuration.AnnotationPersistenceDriver;
import org.lestr.astenn.configuration.CachePersistenceDriver;
import org.lestr.astenn.configuration.CompositePersistenceDriver;
import org.lestr.astenn.configuration.EmbbedXMLDocumentPersistenceDriver;
import org.lestr.astenn.configuration.IPermissionsManager;
import org.lestr.astenn.plugin.IPluginsProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.configuration.IConfiguration;
import org.lestr.astenn.configuration.RAMPersistenceDriver;
import org.lestr.astenn.plugin.IAstennFirstUseListener;
import org.lestr.astenn.plugin.IPersistenceDriver;

/**
 *
 * @author PIBONNIN
 */
public class PluginsManager {


    private static PluginsManager defaultInstance;


    public static synchronized PluginsManager getSingleton() {

        if (defaultInstance == null) {

            defaultInstance = new PluginsManager();

            for (IAstennFirstUseListener astennFirstUseListener : defaultInstance.getRegisteredPlugins(IAstennFirstUseListener.class))
                astennFirstUseListener.astennInstanceStarting();

        }

        return defaultInstance;

    }// END Method getSingleton


    private IConfiguration configuration;


    private IAdvanced advanced;


    private IPermissionsManager permissionsManager;


    private CompositePersistenceDriver persistenceDriver;


    private IPluginsProvider localPluginsProvider;


    private String astennInstanceId;


    private Map<String, Object> properties;


    private Map<Thread, Map<String, Object>> threadSpecificsProperties;


    private PluginsManager() {

        persistenceDriver = new CompositePersistenceDriver(new RAMPersistenceDriver(),
                                                           new CachePersistenceDriver(new EmbbedXMLDocumentPersistenceDriver()),
                                                           new CachePersistenceDriver(new AnnotationPersistenceDriver()));

        astennInstanceId = UUID.randomUUID().toString();

        properties = new HashMap<String, Object>();

        threadSpecificsProperties = new HashMap<Thread, Map<String, Object>>();

        localPluginsProvider = new IPluginsProvider.LocalPluginsProvider();

        configuration = new IConfiguration() {


            @Override
            public String getAstennInstanceId() {

                return astennInstanceId;

            }// END Method getAstennInstanceId


            @Override
            public CompositePersistenceDriver getPersistenceDriver() {

                return persistenceDriver;

            }// END Method getPersistenceDriver


            @Override
            public IPermissionsManager getPermissionsManager() {

                return permissionsManager;

            }// END Method getPermissionsManager


            @Override
            public Map<String, Object> getProperties() {

                return properties;

            }// END Method getProperties


            @Override
            public Map<String, Object> getCurrentThreadSpecificsProperties() {

                if (!threadSpecificsProperties.containsKey(Thread.currentThread()))
                    threadSpecificsProperties.put(Thread.currentThread(), new HashMap<String, Object>());

                return threadSpecificsProperties.get(Thread.currentThread());

            }// END Method getCurrentThreadSpecificsProperties


        };

        advanced = new IAdvanced() {


            @Override
            public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceClass,
                                                                       String pluginImplementationAddress) {

                PluginInterfaceType rslt = null;

                synchronized (PluginsManager.this) {

                    if (pluginInterfaceClass != IPluginsProvider.class)
                        for (IPluginsProvider pluginsProvider : getRegisteredLocalPlugins(IPluginsProvider.class))
                            if (pluginImplementationAddress.startsWith(pluginsProvider.getScheme() + ":")) {
                                rslt = pluginsProvider.getPlugin(pluginInterfaceClass, pluginImplementationAddress);
                                break;
                            }

                    if (pluginImplementationAddress.startsWith(localPluginsProvider.getScheme() + ":"))
                        rslt = localPluginsProvider.getPlugin(pluginInterfaceClass, pluginImplementationAddress);

                }

                return rslt;

            }// END Method getPlugin


        };

        permissionsManager = new IPermissionsManager() {


            private Collection<IPermissionsManagerListener> listeners = new ArrayList<IPermissionsManagerListener>();


            private Collection<Class<?>[]> exposedLocalPlugins = new ArrayList<Class<?>[]>();


            private boolean autoExposeLocalPlugins = false;


            @Override
            public Collection<IPermissionsManagerListener> getListeners() {

                return listeners;

            }// END Method getListeners


            @Override
            public void setAutoExposeLocalPlugins(boolean autoExpose) {

                if (autoExposeLocalPlugins != autoExpose) {

                    autoExposeLocalPlugins = autoExpose;

                    processAutoExposure();

                }

            }// END Method setAutoExposeLocalPlugins


            @Override
            public boolean isAutoExposeLocalPlugins() {

                return autoExposeLocalPlugins;

            }// END Method isAutoExposeLocalPlugins


            @Override
            public void rescanAutoExposedLocalPlugins() {

                processAutoExposure();

            }// END Method rescanAutoExposedLocalPlugins


            @Override
            public void exposeLocalPlugin(Class<?> localPluginInterface,
                                          Class<?> localPluginImplementation) {

                for (Class<?>[] plugin : exposedLocalPlugins)
                    if (plugin.length == 2
                        && plugin[0] == localPluginInterface
                        && plugin[1] == localPluginImplementation)
                        return;

                exposedLocalPlugins.add(new Class<?>[]{localPluginInterface, localPluginImplementation});

                for (IPermissionsManagerListener listener : new ArrayList<IPermissionsManagerListener>(listeners))
                    listener.localPluginExposed(localPluginInterface, localPluginImplementation);

            }// END Method exposeLocalPlugin


            @Override
            public void unexposeLocalPlugin(Class<?> localPluginInterface,
                                            Class<?> localPluginImplementation) {

                for (Class<?>[] plugin : exposedLocalPlugins)
                    if (plugin.length == 2
                        && plugin[0] == localPluginInterface
                        && plugin[1] == localPluginImplementation) {

                        for (IPermissionsManagerListener listener : new ArrayList<IPermissionsManagerListener>(listeners))
                            listener.localPluginUnexposing(localPluginInterface, localPluginImplementation);

                        exposedLocalPlugins.remove(plugin);

                        break;

                    }

            }// END Method unexposeLocalPlugin


            @Override
            public Iterable<Class<?>[]> getExposedLocalPlugins() {

                return exposedLocalPlugins;

            }// END Method getExposedLocalPlugins


        };

    }// END Construtor


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               Class<?> pluginImplementationClass) {

        registerPlugin(pluginInterfaceClass, pluginImplementationClass, persistenceDriver.getReadWritePersistenceDriver());

    }// END Method registerPlugin


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               Class<?> pluginImplementationClass,
                               IPersistenceDriver persistenceDriver) {

        registerPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.getName(), persistenceDriver);

    }// END Method registerPlugin


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               String pluginImplementationAddress) {

        registerPlugin(pluginInterfaceClass, pluginImplementationAddress, persistenceDriver.getReadWritePersistenceDriver());

    }// END Method registerPlugin


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               String pluginImplementationAddress,
                               IPersistenceDriver persistenceDriver) {

        synchronized (this) {

            if (persistenceDriver != null) {

                if (!persistenceDriver.existPluginInterface(pluginInterfaceClass.getName()))
                    persistenceDriver.addPluginInterface(pluginInterfaceClass.getName());

                if (!persistenceDriver.existPluginImplementation(pluginInterfaceClass.getName(), pluginImplementationAddress)) {

                    persistenceDriver.addPluginImplementation(pluginInterfaceClass.getName(), pluginImplementationAddress);

                    if (permissionsManager.isAutoExposeLocalPlugins())
                        processAutoExposure();

                }

            }

        }

    }// END Method registerPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 Class<?> pluginImplementationClass) {

        unregisterPlugin(pluginInterfaceClass, pluginImplementationClass, persistenceDriver.getReadWritePersistenceDriver());

    }// END Method unregisterPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 Class<?> pluginImplementationClass,
                                 IPersistenceDriver persistenceDriver) {

        unregisterPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.getName(), persistenceDriver);

    }// END Method unregisterPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 String pluginImplementationAddress) {

        unregisterPlugin(pluginInterfaceClass, pluginImplementationAddress, persistenceDriver.getReadWritePersistenceDriver());

    }// Method unregisterPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 String pluginImplementationAddress,
                                 IPersistenceDriver persistenceDriver) {

        synchronized (this) {

            if (getConfiguration().getPersistenceDriver() != null
                && getConfiguration().getPersistenceDriver().existPluginInterface(pluginInterfaceClass.getName())
                && getConfiguration().getPersistenceDriver().existPluginImplementation(pluginInterfaceClass.getName(), pluginImplementationAddress)) {

                getConfiguration().getPersistenceDriver().removePluginImplementation(pluginInterfaceClass.getName(), pluginImplementationAddress);

                if (!getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(pluginInterfaceClass.getName()).iterator().hasNext())
                    getConfiguration().getPersistenceDriver().removePluginInterface(pluginInterfaceClass.getName());

            }

        }

    }// Method unregisterPlugin


    public <PluginInterfaceType> Iterable<PluginInterfaceType> getRegisteredLocalPlugins(Class<PluginInterfaceType> pluginInterfaceClass) {

        return getRegisteredPlugins(pluginInterfaceClass, localPluginsProvider);

    }// END Method getRegisteredLocalPlugins


    public <PluginInterfaceType> Iterable<PluginInterfaceType> getRegisteredRemotePlugins(Class<PluginInterfaceType> pluginInterfaceClass) {

        Collection<PluginInterfaceType> rslt = new ArrayList<PluginInterfaceType>();

        if (pluginInterfaceClass != IPluginsProvider.class)
            for (IPluginsProvider pluginsProvider : getRegisteredPlugins(IPluginsProvider.class, localPluginsProvider))
                if (!(pluginsProvider instanceof IPluginsProvider.LocalPluginsProvider))
                    for (PluginInterfaceType plugin : getRegisteredPlugins(pluginInterfaceClass, pluginsProvider))
                        rslt.add(plugin);

        return rslt;

    }// END Method getRegisteredLocalPlugins


    public <PluginInterfaceType> Iterable<PluginInterfaceType> getRegisteredPlugins(Class<PluginInterfaceType> pluginInterfaceClass,
                                                                                    IPluginsProvider pluginsProvider) {

        ArrayList<PluginInterfaceType> rslt = new ArrayList<PluginInterfaceType>();

        synchronized (this) {

            Collection<String> pluginImplementationAddresses = new ArrayList<String>();

            if (getConfiguration().getPersistenceDriver().existPluginInterface(pluginInterfaceClass.getName()))
                for (String pluginImplementationAddress : getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(pluginInterfaceClass.getName()))
                    pluginImplementationAddresses.add(pluginImplementationAddress);

            for (String pluginImplementationAddress : pluginImplementationAddresses)
                if (pluginImplementationAddress.startsWith(pluginsProvider.getScheme() + ":"))
                    rslt.add(pluginsProvider.getPlugin(pluginInterfaceClass, pluginImplementationAddress));

        }

        return rslt;

    }// END Method getRegisteredPlugins


    public <PluginInterfaceType> Iterable<PluginInterfaceType> getRegisteredPlugins(Class<PluginInterfaceType> pluginInterfaceClass) {

        ArrayList<PluginInterfaceType> rslt = new ArrayList<PluginInterfaceType>();

        synchronized (this) {

            if (pluginInterfaceClass != IPluginsProvider.class)
                for (IPluginsProvider pluginsProvider : getRegisteredLocalPlugins(IPluginsProvider.class))
                    for (PluginInterfaceType plugin : getRegisteredPlugins(pluginInterfaceClass, pluginsProvider))
                        rslt.add(plugin);

            for (PluginInterfaceType plugin : getRegisteredPlugins(pluginInterfaceClass, localPluginsProvider))
                rslt.add(plugin);

        }

        return rslt;

    }// END Method getRegisteredPlugins


    public void registerSingletonPlugin(Class<?> pluginInterfaceClass,
                                        Class<?> pluginImplementationClass) {

        if (getRegisteredSingletonPlugin(pluginInterfaceClass) != null)
            unregisterSingletonPlugin(pluginInterfaceClass);

        registerPlugin(pluginInterfaceClass, pluginImplementationClass);

    }// END Method registerSingletonPlugin


    public void registerSingletonPlugin(Class<?> pluginInterfaceClass,
                                        String pluginImplementationAddress) {

        if (getRegisteredSingletonPlugin(pluginInterfaceClass) != null)
            unregisterSingletonPlugin(pluginInterfaceClass);

        registerPlugin(pluginInterfaceClass, pluginImplementationAddress);

    }// END Method registerSingletonPlugin


    public void unregisterSingletonPlugin(Class<?> pluginInterfaceClass) {

        for (String pluginImplementationClass : getConfiguration().getPersistenceDriver().getPluginImplementationsAddresses(pluginInterfaceClass.getName()))
            unregisterPlugin(pluginInterfaceClass, pluginImplementationClass);

    }// END Method unregisterSingletonPlugin


    public <PluginInterfaceType> PluginInterfaceType getRegisteredSingletonPlugin(Class<PluginInterfaceType> pluginInterfaceClass) {

        Iterator<PluginInterfaceType> iterator = getRegisteredPlugins(pluginInterfaceClass).iterator();

        return iterator.hasNext() ? iterator.next() : null;

    }// END Method getRegisteredSingletonPlugin


    public IConfiguration getConfiguration() {

        return configuration;

    }// END Method getConfiguration


    public IAdvanced getAdvanced() {

        return advanced;

    }// END Method getAdvanced


    private void processAutoExposure() {

        if (persistenceDriver != null) {

            Collection<Class<?>[]> actuallyExposedLocalPlugins = new ArrayList<Class<?>[]>();
            for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
                actuallyExposedLocalPlugins.add(plugin);

            for (String pluginInterfaceName : persistenceDriver.getPluginInterfacesNames())
                if (!pluginInterfaceName.equals(IPluginsProvider.class.getName()))
                    for (String pluginImplementationAddress : persistenceDriver.getPluginImplementationsAddresses(pluginInterfaceName))
                        if (pluginImplementationAddress.startsWith("local:"))
                            try {

                                Class<?> pluginInterface = this.getClass().getClassLoader().loadClass(pluginInterfaceName);
                                Class<?> pluginImplementation = this.getClass().getClassLoader().loadClass(pluginImplementationAddress.substring("local:".length()));

                                boolean founded = false;
                                for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
                                    if (plugin[0] == pluginInterface && plugin[1] == pluginImplementation) {
                                        founded = true;
                                        break;
                                    }

                                if (!founded)
                                    permissionsManager.exposeLocalPlugin(pluginInterface, pluginImplementation);

                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(PluginsManager.class.getName()).log(Level.SEVERE, null, ex);
                            }

        }

    }// FIN Method processAutoExposure


    public static interface IAdvanced {


         <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceClass,
                                                             String pluginImplementationAddress);


    }// END Interface IAdvanced


}// END Class PluginsManager


