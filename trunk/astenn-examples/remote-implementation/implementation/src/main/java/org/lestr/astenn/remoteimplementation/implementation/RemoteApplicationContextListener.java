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
package org.lestr.astenn.remoteimplementation.implementation;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.remoteimplementation.api.IService;

/**
 *
 * @author pibonnin
 */
public class RemoteApplicationContextListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent sce) {

        PluginsManager.getSingleton().registerPlugin(IService.class, Service.class);
        PluginsManager.getSingleton().getConfiguration().getPermissionsManager().exposeLocalPlugin(IService.class, Service.class);

    }// END Method contextInitialized


    public void contextDestroyed(ServletContextEvent sce) { }


}// END Class RemoteApplicationContextListener


