/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.withObserverPattern;

/**
 *
 * @author PIBONNIN
 */
public interface IRepositoryListener {


    public void documentAdded(String documentName);

    
    public void documentRemoved(String documentName);


}
