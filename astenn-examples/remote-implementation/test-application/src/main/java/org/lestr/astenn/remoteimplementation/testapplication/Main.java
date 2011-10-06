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

package org.lestr.astenn.remoteimplementation.testapplication;

import java.net.MalformedURLException;
import java.net.URL;
import org.lestr.astenn.remoteimplementation.api.IService;
import org.lestr.astenn.remoteimplementation.api.ServiceLocator;

/**
 *
 * @author pibonnin
 */
public class Main {


    public static void main(String... args) throws MalformedURLException {

        ServiceLocator.getSingleton().setLocation(new URL("http://localhost:8080/implementation-1.0-SNAPSHOT/org.lestr.astenn.remoteimplementation.api.IService/org.lestr.astenn.remoteimplementation.implementation.Service"));

        IService service = ServiceLocator.getSingleton().getService();

        System.out.println(service.sayHello());

    }// END Method main


}// END Class Main
