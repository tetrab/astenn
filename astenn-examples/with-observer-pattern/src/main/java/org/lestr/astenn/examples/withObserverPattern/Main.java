/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.withObserverPattern;

import org.lestr.astenn.PluginsManager;

/**
 *
 * @author pibonnin
 */
public class Main {


    public static void main(String... args){

        install();
        run();

    }// END Method main


    public static void install(){
        
        PluginsManager.getSingleton().registerPlugin(IRepositoryListener.class, ReplicatorPlugin.class);
        PluginsManager.getSingleton().registerPlugin(IRepositoryListener.class, LoggerPlugin.class);

    }// END Method install


    public static void run(){

        Repository repository = new Repository();

        repository.addDocument("Document1.odt");
        repository.addDocument("Document2.odt");
        repository.removeDocument("Document1.odt");

    }// END Method install


}// END Class Main
