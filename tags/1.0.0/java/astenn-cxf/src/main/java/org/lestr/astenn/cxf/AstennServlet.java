/*
 *  Copyright 2011 pibonnin.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.lestr.astenn.cxf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

/**
 *
 * @author pibonnin
 */
public class AstennServlet extends CXFNonSpringServlet {


    static AstennServlet currentAstennServlet;


    private SOAPServer server;


    @Override
    public void loadBus(ServletConfig servletConfig) throws ServletException {

        super.loadBus(servletConfig);

        BusFactory.setDefaultBus(bus);

        server = new SOAPServer(this);

        try {
            server.start();
        } catch (Exception ex) {
            Logger.getLogger(AstennServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// END Method loadBus


    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException {

        String rootURL = request.getRequestURL().toString();
        rootURL = rootURL.substring(0, rootURL.length() - request.getRequestURI().length());
        rootURL = rootURL + getServletContext().getContextPath();

        try {
            getServer().setRootURL(new URL(rootURL));
        } catch (MalformedURLException ex) {
            Logger.getLogger(AstennServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.doPost(request, response);

    }// END Method doPost


    Collection<Server> exposeLocalPlugin(Class<?> pluginInterface,
                                         Class<?> pluginImplementation) throws InstantiationException, IllegalAccessException {

        Collection<Server> rslt = new ArrayList<Server>();

        try {

            ServerFactoryBean servicesFactory = new ServerFactoryBean();

            servicesFactory.setBus(bus);
            servicesFactory.setServiceClass(pluginInterface);
            servicesFactory.setServiceBean(pluginImplementation.newInstance());
            servicesFactory.setAddress("/" + pluginInterface.getName() + "/" + pluginImplementation.getName());

            rslt.add(servicesFactory.create());

            WebService annotationWebService = pluginInterface.getAnnotation(WebService.class);
            if (annotationWebService != null && annotationWebService.name() != null) {

                servicesFactory = new ServerFactoryBean();
                servicesFactory.setServiceClass(pluginInterface);
                servicesFactory.setServiceBean(pluginImplementation.newInstance());
                servicesFactory.setAddress("/" + annotationWebService.name());

                rslt.add(servicesFactory.create());

            }
            
        } catch (Exception ex) {
            Logger.getLogger(AstennServlet.class.getName()).log(Level.WARNING, null, ex);
        }

        return rslt;

    }// END Method exposeLocalPlugin


    /**
     * @return the server
     */
    protected SOAPServer getServer() {

        return server;

    }// END Method getServer


}// END Class AstennServlet


