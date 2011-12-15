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

package org.lestr.astenn.remoteimplementation.api;

import java.net.URL;

import org.lestr.astenn.PluginsManager;

/**
 *
 * @author pibonnin
 */
public class ServiceLocator {


    private static ServiceLocator singleton;


    public static synchronized ServiceLocator getSingleton() {

        return singleton == null ? singleton = new ServiceLocator() : singleton;

    }// END Method getSingleton


    public void setLocation(URL url) {

        PluginsManager.getSingleton().registerSingletonPlugin(IService.class, "soap:" + url.toString());

    }// END Method setLocation


    public IService getService() {

        return PluginsManager.getSingleton().getRegisteredSingletonPlugin(IService.class);

    }// END Method getService


}// END Class ServiceLocator
