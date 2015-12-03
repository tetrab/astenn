/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.cxf;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.configuration.IPermissionsManager;
import org.lestr.astenn.plugin.IServer;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebService;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Cette classe est un greffon Astenn mettant en oeuvre un serveur SOAP s'appuyant sur la librairie CXF.
 * Ce serveur permet l'exposition de greffons Astenn locaux de maniére é ce qu'ils soient invocables é distance.
 * @author Pierre-Antoine Bonnin
 */
public class SOAPServer implements IServer {


    private static final String NAME = "CXF SOAP Server";


    private ServerFactoryBean servicesFactory;


    private Map<String, Collection<Server>> runningServices;


    private int port;


    private String hostname;


    private IPermissionsManager.IPermissionsManagerListener securityManagerListener;


    private AstennServlet astennServlet;


    private URL rootURL;


    public SOAPServer() {

        servicesFactory = null;
        runningServices = new HashMap<String, Collection<Server>>();
        port = 8078;
        astennServlet = null;
        rootURL = null;
        hostname = "localhost";

        securityManagerListener = new IPermissionsManager.IPermissionsManagerListener() {


            @Override
            public void localPluginExposed(Class<?> localPluginInterface,
                                           Class<?> localPluginImplementation) {
                try {
                    exposeLocalPlugin(localPluginInterface, localPluginImplementation);
                } catch (InstantiationException ex) {
                    Logger.getLogger(SOAPServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(SOAPServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }// END Method localPluginExposed


            @Override
            public void localPluginUnexposing(Class<?> localPluginInterface,
                                              Class<?> localPluginImplementation) {

                unexposeLocalPlugin(localPluginInterface, localPluginImplementation);

            }// END Method localPluginUnexposing


        };

    }// END Constructor


    SOAPServer(AstennServlet astennServlet) {

        this();

        this.astennServlet = astennServlet;

    }// END Constructor


    @Override
    public String getName() {

        return NAME;

    }// END Method getName


    /**
     * @return Port TCP utilisé par le serveur.
     */
    public int getPort() {

        return port;

    }// END Method getPort


    /**
     * @param port TCP utilisé par le serveur.
     */
    public void setPort(int port) {

        this.port = port;

    }// END Method setPort


    /**
     * @return the hostname
     */
    public String getHostname() {

        return hostname;

    }// END Method getHostname


    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {

        this.hostname = hostname;

    }// END Method setHostname


    @Override
    public void start() throws Exception {

        // Récupération du gestionnaire de permissions d'Astenn
        IPermissionsManager permissionsManager = PluginsManager.getSingleton().getConfiguration().getPermissionsManager();

        // Exposition SOAP de l'ensemble des greffons enregistrés dans le gestionnaire de permissions
        // comme devant étre exposés
        for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
            exposeLocalPlugin(plugin[0], plugin[1]);

        // Démarrage de la surveillance des événements survenant au sein du gestionnaire de permissions
        // afin que de bien exposer ou retirer les greffons lorsque demandé
        permissionsManager.getListeners().add(securityManagerListener);

    }// END Method start


    @Override
    public void stop() throws Exception {

        // Récupération du gestionnaire de permissions d'Astenn
        IPermissionsManager permissionsManager = PluginsManager.getSingleton().getConfiguration().getPermissionsManager();

        // Arrét de la surveillance des événements survenant au sein du gestionnaire de permissions
        permissionsManager.getListeners().remove(securityManagerListener);

        // Arrêt de l'exposition SOAP de l'ensemble des greffons enregistrés dans le gestionnaire de
        // permissions comme devant étre exposés
        for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
            unexposeLocalPlugin(plugin[0], plugin[1]);

    }// END Method stop


    @Override
    public String getPluginRemoteAddress(Class<?> pluginInterfaceClass,
                                         Class<?> pluginImplementationClass) {

        return "soap:" + (rootURL == null
                          ? "http://" + getHostname() + ":" + getPort()
                          : rootURL.toString()) + "/" + pluginInterfaceClass.getName() + "/" + pluginImplementationClass.getName();

    }// END Method getPluginRemoteAddress


    /**
     * @return the rootURL
     */
    void setRootURL(URL rootURL) {

        this.rootURL = rootURL;

    }// END Method rootURL


    /**
     * Expose un greffon en SOAP a l'aide de CXF.
     * @param pluginInterface Interface de définition du greffon é exposer.
     * @param pluginImplementation Classe d'implémentation du greffon é exposer.
     * @throws InstantiationException La classe d'implémentation du greffon de dispose pas de constructeur vide
     * ou a levé une exception au cours de son invocation.
     * @throws IllegalAccessException La classe d'implémentation du greffon ne dispose pas de constructeur public.
     */
    private void exposeLocalPlugin(Class<?> pluginInterface,
                                   Class<?> pluginImplementation) throws InstantiationException, IllegalAccessException {

        String localAdress = "local://" + pluginInterface.getName() + "/" + pluginImplementation.getName();

        if (!runningServices.containsKey(localAdress)) {

            Collection<Server> servers = new ArrayList<Server>();

            if (astennServlet != null)
                servers.addAll(astennServlet.exposeLocalPlugin(pluginInterface, pluginImplementation));
            else {

                servicesFactory = getServerFactoryBean(pluginInterface);

                servicesFactory.setServiceClass(pluginInterface);
                servicesFactory.setServiceBean(pluginImplementation.newInstance());
                servicesFactory.setAddress("http://127.0.0.1:" + getPort() + "/" + pluginInterface.getName() + "/" + pluginImplementation.getName());

                servers.add(servicesFactory.create());

                WebService annotationWebService = pluginInterface.getAnnotation(WebService.class);
                if (annotationWebService != null && annotationWebService.name() != null) {

                    servicesFactory = getServerFactoryBean(pluginInterface);
                    servicesFactory.setServiceClass(pluginInterface);
                    servicesFactory.setServiceBean(pluginImplementation.newInstance());
                    servicesFactory.setAddress("http://127.0.0.1:" + getPort() + "/" + annotationWebService.name());

                    servers.add(servicesFactory.create());

                }

            }

            runningServices.put(localAdress, servers);

        }

    }// END Method exposeLocalPlugin


    /**
     * Arréte l'exposition d'un greffon en SOAP via CXF.
     * @param pluginInterface Interface de définition du greffon exposé.
     * @param pluginImplementation Classe d'implémentation du greffon exposé.
     */
    private void unexposeLocalPlugin(Class<?> pluginInterface,
                                     Class<?> pluginImplementation) {

        String localAdress = "local://" + pluginInterface.getName() + "/" + pluginImplementation.getName();

        if (runningServices.containsKey(localAdress))
            for (Server server : runningServices.get(localAdress))
                server.stop();

        runningServices.remove(localAdress);

    }// END Method unexposeLocalPlugin


    private ServerFactoryBean getServerFactoryBean(Class<?> pluginInterface) {

        ServerFactoryBean rslt;

        String jaxWsStrict = PluginsManager.getSingleton().getConfiguration().getProperties().containsKey("JAX-WS STRICT")
                             ? PluginsManager.getSingleton().getConfiguration().getProperties().get("JAX-WS STRICT").toString()
                             : "auto";

        if (jaxWsStrict.equalsIgnoreCase("yes"))
            rslt = new JaxWsServerFactoryBean();
        else if (jaxWsStrict.equalsIgnoreCase("no"))
            rslt = new ServerFactoryBean();
        else {


            WebService annotationWebService = pluginInterface.getAnnotation(WebService.class);

            rslt = annotationWebService != null && annotationWebService.name() != null
                   ? new JaxWsServerFactoryBean()
                   : new ServerFactoryBean();

        }

        return rslt;

    }// FIN Méthode getServerFactoryBean


}// END Class AstennServer


