/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.examples.annotation;

import org.lestr.astenn.configuration.AnnotationPersistenceDriver.Plugin;

/**
 *
 * @author pibonnin
 */
@Plugin(IPlugin.class)
public class APlugin implements IPlugin {


    public String getMessage() {

        return "Hello, I'm the A-Plugin !";

    }// END Method getMessage


}// END Class APlugin
