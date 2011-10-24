/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.withObserverPattern;

/**
 *
 * @author PIBONNIN
 */
public class ReplicatorPlugin implements IRepositoryListener {


    public void documentAdded(String documentName) {

        System.out.println("I'm the plugin \"Replicator\". I've detected that a document have been added to the repository and I will replicate it.");

        // ...

    }// END Method documentAdded


    public void documentRemoved(String documentName) { }


}// END Class ARepositoryListener
