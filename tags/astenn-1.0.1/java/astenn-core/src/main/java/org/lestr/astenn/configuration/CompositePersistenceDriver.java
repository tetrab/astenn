/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author PIBONNIN
 */
public class CompositePersistenceDriver implements IPersistenceDriver {


    private IPersistenceDriver readWritePersistenceDriver;


    private Collection<IPersistenceDriver> readOnlyPersistenceDrivers;


    public CompositePersistenceDriver() {

        readWritePersistenceDriver = new RAMPersistenceDriver();
        readOnlyPersistenceDrivers = new ArrayList<IPersistenceDriver>();

    }// END Constructor


    public CompositePersistenceDriver(IPersistenceDriver readWritePersistenceDriver,
                                      IPersistenceDriver... readOnlyPersistenceDrivers) {

        this.readWritePersistenceDriver = readWritePersistenceDriver;
        this.readOnlyPersistenceDrivers = new ArrayList<IPersistenceDriver>();

        this.readOnlyPersistenceDrivers.addAll(Arrays.asList(readOnlyPersistenceDrivers));

    }// END Constructor


    /**
     * @return the ReadWritePersistenceDriver
     */
    public IPersistenceDriver getReadWritePersistenceDriver() {

        return readWritePersistenceDriver;

    }// END Method getReadWritePersistenceDriver


    /**
     * @param ReadWritePersistenceDriver the ReadWritePersistenceDriver to set
     */
    public void setReadWritePersistenceDriver(IPersistenceDriver ReadWritePersistenceDriver) {

        this.readWritePersistenceDriver = ReadWritePersistenceDriver;

    }// END Method setReadWritePersistenceDriver


    /**
     * @return the ReadOnlyPersistenceDrivers
     */
    public Collection<IPersistenceDriver> getReadOnlyPersistenceDrivers() {

        return readOnlyPersistenceDrivers;

    }// END Method getReadOnlyPersistenceDrivers


    @Override
    public void addPluginInterface(String pluginInterfaceName) {

        readWritePersistenceDriver.addPluginInterface(pluginInterfaceName);

    }// END Method addPluginInterface


    @Override
    public void removePluginInterface(String pluginInterfaceName) {

        readWritePersistenceDriver.removePluginInterface(pluginInterfaceName);

    }// END Method removePluginInterface


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        if (readWritePersistenceDriver.existPluginInterface(pluginInterfaceName))
            return true;

        for (IPersistenceDriver driver : readOnlyPersistenceDrivers)
            if (driver.existPluginInterface(pluginInterfaceName))
                return true;

        return false;

    }// END Method existPluginInterface


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        readWritePersistenceDriver.addPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

    }// END Method addPluginImplementation


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        if (readWritePersistenceDriver.existPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
            readWritePersistenceDriver.removePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

    }// END Method removePluginImplementation


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        if (readWritePersistenceDriver.existPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
            return true;

        for (IPersistenceDriver driver : readOnlyPersistenceDrivers)
            if (driver.existPluginInterface(pluginInterfaceName)
                && driver.existPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
                return true;

        return false;

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        ArrayList<String> rslt = new ArrayList<String>();

        for (String interfaceName : readWritePersistenceDriver.getPluginInterfacesNames())
            rslt.add(interfaceName);

        for (IPersistenceDriver driver : readOnlyPersistenceDrivers)
            for (String interfaceName : driver.getPluginInterfacesNames())
                if (!rslt.contains(interfaceName))
                    rslt.add(interfaceName);

        return rslt;

    }// END Method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        ArrayList<String> rslt = new ArrayList<String>();

        if (readWritePersistenceDriver.existPluginInterface(pluginInterfaceName))
            for (String implementation : readWritePersistenceDriver.getPluginImplementationsAddresses(pluginInterfaceName))
                rslt.add(implementation);

        for (IPersistenceDriver driver : readOnlyPersistenceDrivers)
            if (driver.existPluginInterface(pluginInterfaceName))
                for (String implementation : driver.getPluginImplementationsAddresses(pluginInterfaceName))
                    if (!rslt.contains(implementation))
                        rslt.add(implementation);

        return rslt;

    }// END Method getPluginImplementationsStrings


}// END Class CompositePersistenceDriver
