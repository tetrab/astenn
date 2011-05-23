/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.cxf;

import org.lestr.astenn.plugin.IPluginsProvider;
import java.util.HashMap;
import java.util.Map;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.lestr.astenn.PluginsManager;

/**
 * Cette classe est un greffon Astenn s'appuyant sur CXF pour permettre l'invocation de greffons de manière
 * distante via le protocole SOAP.
 * @author Pierre-Antoine Bonnin
 */
public class SOAPPluginsProvider implements IPluginsProvider {


    private HashMap<Class<?>, Object> proxies;


    public SOAPPluginsProvider() {

        proxies = new HashMap<Class<?>, Object>();

    }// END Constructor


    @Override
    public String getScheme() {

        return "soap";

    }// END Method getScheme


    @Override
    public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType,
                                                               String pluginImplementationAddress) {

        if (!proxies.containsValue(pluginInterfaceType)) {

            ClientProxyFactoryBean factory = new ClientProxyFactoryBean();

            if (PluginsManager.getSingleton().getConfiguration().getProperties().containsKey("USERNAME")
                && PluginsManager.getSingleton().getConfiguration().getProperties().containsKey("PASSWORD")) {

                factory.setUsername(PluginsManager.getSingleton().getConfiguration().getProperties().get("USERNAME").toString());
                factory.setPassword(PluginsManager.getSingleton().getConfiguration().getProperties().get("PASSWORD").toString());

            }

            factory.setServiceClass(pluginInterfaceType);
            factory.setAddress(pluginImplementationAddress.substring((getScheme() + ":").length()));
            proxies.put(pluginInterfaceType, factory.create());

            if (PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().containsKey("COOKIES")) {

                HTTPConduit conduit = (HTTPConduit) ClientProxy.getClient(proxies.get(pluginInterfaceType)).getConduit();

                if (conduit.getClient() == null)
                    conduit.setClient(new HTTPClientPolicy());

                Map<String, String> cookies = (Map<String, String>) PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().get("COOKIES");

                if (!cookies.isEmpty()) {

                    String cookie = "";
                    for (String cle : cookies.keySet())
                        cookie = cookie + cookies.get(cle) + ";";
                    cookie = cookie.substring(0, cookie.length() - 1);

                    conduit.getClient().setCookie(cookie);

                }

            }

        }

        return (PluginInterfaceType) proxies.get(pluginInterfaceType);

    }// END Method getPlugin


}// END Class ARSPluginsManager


