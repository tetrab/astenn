/*
 *  Copyright 2011 PIBONNIN.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.lestr.astenn.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;
import org.lestr.astenn.plugin.IPersistenceDriver;

/**
 *
 * @author PIBONNIN
 */
public class PropertiesPersistenceDriver implements IPersistenceDriver {


    private Properties properties;


    public PropertiesPersistenceDriver() {

        properties = new Properties();

    }// END Constructor


    public PropertiesPersistenceDriver(Properties properties) {

        this.properties = properties;

    }// END Constructor


    /**
     * @return the properties
     */
    public Properties getProperties() {

        return properties;

    }// END Method getProperties


    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }// END Method setProperties


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        properties.setProperty("Interface-" + pluginInterfaceName, "");

    }// END Method addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        for(String implementation : getPluginImplementationsAddresses(pluginInterfaceName))
            removePluginImplementation(pluginInterfaceName, implementation);

        properties.remove("Interface-" + pluginInterfaceName);
        
    }// END Method removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return properties.getProperty("Interface-" + pluginInterfaceName) != null;

    }// END Method existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        properties.setProperty("Implementation-" + UUID.randomUUID().toString(), pluginInterfaceName + ";" + pluginImplementationAddress);

    }// END Method addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        String cleProprieteASupprimer = null;

        for (String cle : properties.stringPropertyNames())
            if (cle.startsWith("Implementation-")
                && properties.getProperty(cle).equals(pluginInterfaceName + ";" + pluginImplementationAddress)) {
                cleProprieteASupprimer = cle;
                break;
            }

        if (cleProprieteASupprimer != null)
            properties.remove(cleProprieteASupprimer);

    }// END Method removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        for (String cle : properties.stringPropertyNames())
            if (cle.startsWith("Implementation-")
                && properties.getProperty(cle).equals(pluginInterfaceName + ";" + pluginImplementationAddress))
                return true;

        return false;

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        Collection<String> rslt = new ArrayList<String>();

        for (String cle : properties.stringPropertyNames())
            if (cle.startsWith("Interface-"))
                rslt.add(cle.substring("Interface-".length()));

        return rslt;

    }// END Method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Collection<String> rslt = new ArrayList<String>();

        for (String cle : properties.stringPropertyNames())
            if (cle.startsWith("Implementation-")
                && properties.getProperty(cle).startsWith(pluginInterfaceName + ";"))
                rslt.add(cle.substring(cle.indexOf(";") + 1));

        return rslt;

    }// END Method getPluginImplementationsAddresses


}// END Class PropertiesPersistenceDriver


