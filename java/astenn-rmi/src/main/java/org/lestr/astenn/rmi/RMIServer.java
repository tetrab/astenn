/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.rmi;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.astenn.PluginsManager;
import org.lestr.astenn.configuration.IPermissionsManager;
import org.lestr.astenn.plugin.IServer;

public class RMIServer implements IServer {


    private static final String NAME = "RMI Server";


    private int port;


    private IPermissionsManager.IPermissionsManagerListener securityManagerListener;


    private Registry registry;


    public RMIServer() {

        port = 1099;
        registry = null;

        securityManagerListener = new IPermissionsManager.IPermissionsManagerListener() {


            @Override
            public void localPluginExposed(Class<?> localPluginInterface,
                                           Class<?> localPluginImplementation) {
                try {

                    if (Remote.class.isAssignableFrom(localPluginImplementation))
                        registry.rebind(localPluginInterface.getName() + "/" + localPluginImplementation.getName(), (Remote) localPluginImplementation.newInstance());

                } catch (InstantiationException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }// END Method localPluginExposed


            @Override
            public void localPluginUnexposing(Class<?> localPluginInterface,
                                              Class<?> localPluginImplementation) {
                try {

                    registry.unbind(localPluginInterface.getName() + "/" + localPluginImplementation.getName());

                } catch (RemoteException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NotBoundException ex) {
                    Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                }

            }// END Method localPluginUnexposing


        };

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
     * @param Port TCP utilisé par le serveur.
     */
    public void setPort(int port) {

        this.port = port;

    }// END Method setPort


    @Override
    public void start() throws Exception {

        registry = port == 1099 ? LocateRegistry.getRegistry() : LocateRegistry.createRegistry(port);

        // Récupération du gestionnaire de permissions d'Astenn
        IPermissionsManager permissionsManager = PluginsManager.getSingleton().getConfiguration().getPermissionsManager();

        for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
            if (Remote.class.isAssignableFrom(plugin[1]))
                registry.rebind(plugin[0].getName() + "/" + plugin[1].getName(), (Remote) plugin[1].newInstance());

        // Démarrage de la surveillance des événements survenant au sein du gestionnaire de permissions
        // afin que de bien exposer ou retirer les greffons lorsque demandé
        permissionsManager.getListeners().add(securityManagerListener);

    }// END Method start


    @Override
    public void stop() throws Exception {

        for (String name : registry.list())
            LocateRegistry.getRegistry().unbind(name);

        // Récupération du gestionnaire de permissions d'Astenn
        IPermissionsManager permissionsManager = PluginsManager.getSingleton().getConfiguration().getPermissionsManager();

        for (Class<?>[] plugin : permissionsManager.getExposedLocalPlugins())
            registry.unbind(plugin[0].getName() + "/" + plugin[1].getName());

        registry = null;

    }// END Method stop


    public String getPluginRemoteAddress(Class<?> pluginInterfaceClass,
                                         Class<?> pluginImplementationClass) {

        return "rmi:127.0.0.1:" + getPort() + "/" + pluginInterfaceClass.getName() + "/" + pluginImplementationClass.getName();

    }// END Method getPluginRemoteAddress


}// END Class AstennServer


