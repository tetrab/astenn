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

package org.lestr.astenn.standalonecxf.localapplication;

import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.standalonecxf.api.IPlugin;

/**
 *
 * @author pibonnin
 */
public class Main {


    public static void main(String... args) {

        PluginsManager.getSingleton().registerPlugin(IPlugin.class, LocalPlugin.class);
        PluginsManager.getSingleton().registerPlugin(IPlugin.class, "soap:http://localhost:8067/org.lestr.astenn.standalonecxf.api.IPlugin/org.lestr.astenn.standalonecxf.remoteapplication.RemotePlugin");

        for(IPlugin plugin : PluginsManager.getSingleton().getRegisteredPlugins(IPlugin.class))
            System.out.println(plugin.sayHello());

    }// END Method main


}// END Class Main
