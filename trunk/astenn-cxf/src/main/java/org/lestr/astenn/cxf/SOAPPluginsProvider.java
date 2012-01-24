/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.cxf;

import org.lestr.astenn.plugin.IPluginsProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
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
            
            String jaxWsStrict = PluginsManager.getSingleton().getConfiguration().getProperties().containsKey("JAX-WS STRICT")
                                 ? PluginsManager.getSingleton().getConfiguration().getProperties().get("JAX-WS STRICT").toString()
                                 : "auto";

            ClientProxyFactoryBean factory;

            if (jaxWsStrict.equalsIgnoreCase("yes"))
                factory = new JaxWsProxyFactoryBean();
            else if (jaxWsStrict.equalsIgnoreCase("no"))
                factory = new ClientProxyFactoryBean();
            else {


                WebService annotationWebService = pluginInterfaceType.getAnnotation(WebService.class);

                factory = annotationWebService != null && annotationWebService.name() != null
                          ? new JaxWsProxyFactoryBean()
                          : new ClientProxyFactoryBean();

            }

            if (PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().containsKey("USERNAME")
                && PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().containsKey("PASSWORD")) {

                factory.setUsername(PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().get("USERNAME").toString());
                factory.setPassword(PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().get("PASSWORD").toString());

            }

            factory.setServiceClass(pluginInterfaceType);
            factory.setAddress(pluginImplementationAddress.substring((getScheme() + ":").length()));
            proxies.put(pluginInterfaceType, factory.create());

            Client client = ClientProxy.getClient(proxies.get(pluginInterfaceType));

            if (PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().containsKey("HEADERS")) {

                Map<String, List<String>> enTetes = (Map<String, List<String>>) PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().get("HEADERS");

                client.getRequestContext().put(Message.PROTOCOL_HEADERS, enTetes);

            }

            if (PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().containsKey("COOKIES")) {

                HTTPConduit conduit = (HTTPConduit) client.getConduit();

                if (conduit.getClient() == null) {
                    HTTPClientPolicy police = new HTTPClientPolicy();
                    police.setConnectionTimeout(36000);
                    police.setAllowChunking(false);
                    conduit.setClient(police);
                }

                Map<String, String> cookies = (Map<String, String>) PluginsManager.getSingleton().getConfiguration().getCurrentThreadSpecificsProperties().get("COOKIES");

                if (!cookies.isEmpty()) {

                    String cookie = "";
                    for (String cle : cookies.keySet())
                        cookie = cookie + cle + "=" + cookies.get(cle) + ";";
                    cookie = cookie.substring(0, cookie.length() - 1);

                    conduit.getClient().setCookie(cookie);

                }

            }

        }

        return (PluginInterfaceType) proxies.get(pluginInterfaceType);

    }// END Method getPlugin


}// END Class ARSPluginsManager


