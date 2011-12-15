/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.withObserverPattern;

import org.lestr.astenn.PluginsManager;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author PIBONNIN
 */
public class Repository {


    private Collection<IRepositoryListener> listeners;


    public Repository(){

        listeners = new ArrayList<IRepositoryListener>();

    }// END Constructor


    public void addListener(IRepositoryListener listener){

        listeners.add(listener);

    }// END Method addListener


    public void removeListener(IRepositoryListener listener){

        listeners.remove(listener);

    }// END Method removeListener


    public Iterable<IRepositoryListener> getListeners(){
        
        Collection<IRepositoryListener> rslt = new ArrayList<IRepositoryListener>(listeners);

        for(IRepositoryListener listener : PluginsManager.getSingleton().getRegisteredPlugins(IRepositoryListener.class))
            rslt.add(listener);

        return rslt;

    }// END Method removeListener


    public void addDocument(String documentName){

        // ...

        for(IRepositoryListener listener : getListeners())
            listener.documentAdded(documentName);

    }// END Method addDocument


    public void removeDocument(String documentName){

        // ...

        for(IRepositoryListener listener : getListeners())
            listener.documentRemoved(documentName);

    }// END Method addDocument


}// END Class Repository
