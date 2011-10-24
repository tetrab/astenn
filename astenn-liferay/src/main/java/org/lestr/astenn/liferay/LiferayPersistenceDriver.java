/*
 *  Copyright 2011 pibonnin.
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
package org.lestr.astenn.liferay;

import com.liferay.portal.service.WebsiteServiceUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.configuration.PropertiesPersistenceDriver;
import org.lestr.astenn.plugin.IPersistenceDriver;

/**
 *
 * @author pibonnin
 */
public class LiferayPersistenceDriver implements IPersistenceDriver {


    private static LiferayPersistenceDriver singleton;


    public static synchronized LiferayPersistenceDriver getSingleton() {

        return singleton == null ? singleton = new LiferayPersistenceDriver() : singleton;

    }// END Method getSingleton


    private Map<Thread, Long> currentsWebsitesIds = new HashMap<Thread, Long>();


    public synchronized void openWebsite(long currentWebsiteId) {

        this.currentsWebsitesIds.put(Thread.currentThread(), currentWebsiteId);

    }// END Method openWebsite


    public synchronized void closeWebsite() {

        currentsWebsitesIds.remove(Thread.currentThread());

    }// END Method closeWebsite


    private PropertiesPersistenceDriver persistenceDriver;


    private LiferayPersistenceDriver() {

        persistenceDriver = new PropertiesPersistenceDriver();

    }// END Constructor


    public void addPluginInterface(String pluginInterfaceName) {

        try {

            load();

            persistenceDriver.addPluginInterface(pluginInterfaceName);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method addPluginInterface


    public void removePluginInterface(String pluginInterfaceName) {

        try {

            load();

            persistenceDriver.removePluginInterface(pluginInterfaceName);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method removePluginInterface


    public boolean existPluginInterface(String pluginInterfaceName) {

        boolean rslt = false;

        try {

            load();

            rslt = persistenceDriver.existPluginInterface(pluginInterfaceName);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method existPluginInterface


    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {

        try {

            load();

            persistenceDriver.addPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method addPluginImplementation


    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {

        try {

            load();

            persistenceDriver.removePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method removePluginImplementation


    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        boolean rslt = false;

        try {

            load();

            rslt = persistenceDriver.existPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method existPluginImplementation


    public Iterable<String> getPluginInterfacesNames() {

        Iterable<String> rslt = new ArrayList<String>();

        try {

            load();

            rslt = persistenceDriver.getPluginInterfacesNames();

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method getPluginInterfacesNames


    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Iterable<String> rslt = new ArrayList<String>();

        try {

            load();

            rslt = persistenceDriver.getPluginImplementationsAddresses(pluginInterfaceName);

            save();

        } catch (Exception ex) {
            Logger.getLogger(LiferayPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method getPluginImplementationsAdresses


    private void load() throws Exception {

        ExpandoBridge properties = WebsiteServiceUtil.getWebsite(currentsWebsitesIds.get(Thread.currentThread())).getExpandoBridge();

        if (!properties.hasAttribute("astenn-liferay")) {
            properties.addAttribute("astenn-liferay");
            properties.setAttribute("astenn-liferay", "");
        }

        persistenceDriver.getProperties().load(new StringReader(properties.getAttribute("astenn-liferay").toString()));

    }// END Method load


    private void save() throws Exception {

        ExpandoBridge properties = WebsiteServiceUtil.getWebsite(currentsWebsitesIds.get(Thread.currentThread())).getExpandoBridge();

        StringWriter sw = new StringWriter();
        persistenceDriver.getProperties().store(sw, null);

        properties.setAttribute("astenn-liferay", sw.getBuffer().toString());

        properties = null;

    }// END Method save


}// END Class LiferayPersistenceDriver


