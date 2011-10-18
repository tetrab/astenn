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
package org.lestr.astenn.bus.impl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author pibonnin
 */
@WebService
public interface IBusEndpoint extends Remote {


    @WebMethod
    String getEndpointId(String busId) throws RemoteException;


    @WebMethod
    String[] getPluginInterfacesNames(String busId) throws RemoteException;


    @WebMethod
    String[] getPluginImplementationsAddresses(String busId, String pluginInterfaceClassName) throws RemoteException;


    @WebMethod
    String[] getKnowBusEndpointsAddresses(String busId) throws RemoteException;


    @WebMethod
    void declareBusEndpointAddress(String busId, String address) throws RemoteException;


}// FIN Interface ICXFBusService


