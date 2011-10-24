/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.withObserverPattern;

/**
 *
 * @author PIBONNIN
 */
public class LoggerPlugin implements IRepositoryListener {


    public void documentAdded(String documentName) {

        System.out.println("I'm the plugin \"Logger\". I inform you that a document have been added to the repository.");

    }// END Method documentAdded


    public void documentRemoved(String documentName) {

        System.out.println("I'm the plugin \"Logger\". I inform you that a document have been removed from the repository.");

    }// END Method documentAdded


}// END Class ARepositoryListener
