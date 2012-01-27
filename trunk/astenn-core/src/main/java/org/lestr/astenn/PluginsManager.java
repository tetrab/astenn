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


    private CompositePersistenceDriver compositePersistenceDriver;


    private IPersistenceDriver rootPersistenceDriver;


    private IPluginsProvider localPluginsProvider;


    private String astennInstanceId;


    private Map<String, Object> properties;


    private Map<Thread, Map<String, Object>> threadSpecificsProperties;


    private PluginsManager() {

        compositePersistenceDriver = new CompositePersistenceDriver(new RAMPersistenceDriver(),
                                                                    new CachePersistenceDriver(new EmbbedXMLDocumentPersistenceDriver()),
                                                                    new CachePersistenceDriver(new AnnotationPersistenceDriver()));

        rootPersistenceDriver = new EquivalentsPersistenceDriver(compositePersistenceDriver);

        astennInstanceId = UUID.randomUUID().toString();
        properties = new HashMap<String, Object>();
        threadSpecificsProperties = new HashMap<Thread, Map<String, Object>>();
        localPluginsProvider = new IPluginsProvider.LocalPluginsProvider();
        permissionsManager = new PermissionsManagerImpl();

        configuration = new IConfiguration() {


            @Override
            public String getAstennInstanceId() {

                return astennInstanceId;

            }// END Method getAstennInstanceId


            @Override
            public CompositePersistenceDriver getPersistenceDriver() {

                return compositePersistenceDriver;

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

    }// END Construtor


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               Class<?> pluginImplementationClass) {

        registerPlugin(pluginInterfaceClass,
                       pluginImplementationClass,
                       getConfiguration().getPersistenceDriver().getReadWritePersistenceDriver());

    }// END Method registerPlugin


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               Class<?> pluginImplementationClass,
                               IPersistenceDriver persistenceDriver) {

        registerPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.getName(), persistenceDriver);

    }// END Method registerPlugin


    public void registerPlugin(Class<?> pluginInterfaceClass,
                               String pluginImplementationAddress) {

        registerPlugin(pluginInterfaceClass,
                       pluginImplementationAddress,
                       getConfiguration().getPersistenceDriver().getReadWritePersistenceDriver());

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
                        permissionsManager.rescanAutoExposedLocalPlugins();

                }

            }

        }

    }// END Method registerPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 Class<?> pluginImplementationClass) {

        unregisterPlugin(pluginInterfaceClass,
                         pluginImplementationClass,
                         getConfiguration().getPersistenceDriver().getReadWritePersistenceDriver());

    }// END Method unregisterPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 Class<?> pluginImplementationClass,
                                 IPersistenceDriver persistenceDriver) {

        unregisterPlugin(pluginInterfaceClass, "local:" + pluginImplementationClass.getName(), persistenceDriver);

    }// END Method unregisterPlugin


    public void unregisterPlugin(Class<?> pluginInterfaceClass,
                                 String pluginImplementationAddress) {

        unregisterPlugin(pluginInterfaceClass, pluginImplementationAddress, compositePersistenceDriver.getReadWritePersistenceDriver());

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

            if (rootPersistenceDriver.existPluginInterface(pluginInterfaceClass.getName()))
                for (String pluginImplementationAddress : rootPersistenceDriver.getPluginImplementationsAddresses(pluginInterfaceClass.getName()))
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


    public static interface IAdvanced {


         <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceClass,
                                                             String pluginImplementationAddress);


    }// END Interface IAdvanced


}// END Class PluginsManager


