/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.protocol.ars;

import org.lestr.astenn.plugin.IPluginsProvider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.genver.reflection.ProxyFactory;
import org.lestr.genver.reflection.ProxyFactory.InvocationWriter;

/**
 *
 * @author PIBONNIN
 */
public class ARSPluginsProvider implements IPluginsProvider {


    private HashMap<Class<?>, Object> proxies;
    private ProxyFactory proxyFactory;


    public ARSPluginsProvider()
    {

        proxies = new HashMap<Class<?>, Object>();
        proxyFactory = new ProxyFactory();

    }// END Constructor


    @Override
    public String getScheme() {

        return "ars";

    }// END Method getScheme


    @Override
    public <PluginInterfaceType> PluginInterfaceType getPlugin(Class<PluginInterfaceType> pluginInterfaceType, String pluginImplementationAddress) {

        try {

            URI pluginImplementationURI = new URI(pluginImplementationAddress.substring((getScheme() + ":").length()));

            final String implementationClassName = URLDecoder.decode(pluginImplementationURI.getPath().substring(1), "UTF-8");

            if(!proxies.containsValue(pluginInterfaceType))
                try {

                    Class<?> proxyClass = proxyFactory.constructProxy(pluginInterfaceType, ARSClient.class, "astenn_ARSClient", new InvocationWriter() {

                        @Override
                        public String writeInvocation(Class<?> proxiedClass,
                                                      Method proxiedMethod,
                                                      String argsVariableName) {

                            return "astenn_ARSClient.invokeServiceMethod(typeof(" + proxiedClass.getName() + "), \"" + implementationClassName + "\", \"" + proxiedMethod.getName() + "\", " + argsVariableName + ");\n";

                        }// END Method writeInvocation

                    });

                    proxies.put(pluginInterfaceType, proxyClass.getConstructor(new Class<?>[0]).newInstance(new Object[0]));

                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
                }

            ARSClient client = new ARSClient();
            client.setHost(pluginImplementationURI.getHost());
            client.setPort(pluginImplementationURI.getPort());
            try {
                client.connect();
            } catch (IOException ex) {
                Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                proxies.get(pluginInterfaceType).getClass().getField("astenn_ARSClient").set(proxies.get(pluginInterfaceType), client);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ARSPluginsProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (PluginInterfaceType)proxies.get(pluginInterfaceType);

    }// END Method getPlugin


}// END Class ARSPluginsManager
