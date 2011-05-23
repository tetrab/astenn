/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.lestr.astenn.protocol.ars;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lestr.genver.data.serialization.Reader;
import org.lestr.genver.data.serialization.Writer;

/**
 *
 * @author pibonnin
 */
public class ARSClient {


    private String host;
    private int port;
    private Reader reader;
    private Writer writer;
    private InputStream input;
    private OutputStream output;
    private Socket socket;
    private int freeServiceId;
    private HashMap<Class<?>, HashMap<String, Integer>> servicesIds;


    public ARSClient() {

        reader = new Reader();
        writer = new Writer();
        freeServiceId = 0;
        servicesIds = new HashMap<Class<?>, HashMap<String, Integer>>();
        host = "127.0.0.1";
        port = 4596;
        socket = null;
        input = null;
        output = null;

    }// END Constructor


    /**
     * @param host the hostname to set
     */
    public void setHost(String host) {
        this.host = host;
    }


    /**
     * @return the hostname
     */
    public String getHost() {
        return host;
    }
    

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }


    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }


    public void connect() throws IOException {

        socket = new Socket(getHost(), getPort());

        output = socket.getOutputStream();
        input = socket.getInputStream();

    }// END Method Connect


    public void disconnect() throws IOException {

        input.close();
        input = null;

        output.close();
        output = null;

        socket.close();

    }// END Method Disconnect


    public Object invokeServiceMethod(Class interfaceType, String implementationClassName, String methodName, Object[] args) throws IOException, NoSuchFieldException, Exception {

        Object rslt = null;

        try {

            writer.open(output);
            reader.open(input);

            if (!servicesIds.containsKey(interfaceType))
                declareService(interfaceType, implementationClassName);

            writer.write(byte.class, ARSServer.INVOKE_SERVICE_METHOD);
            writeIndex(servicesIds.get(interfaceType).get(implementationClassName));

            int methodId = 0;
            for (int i = 0; i <= interfaceType.getMethods().length - 1; i = i + 1) {
                if (interfaceType.getMethods()[i].getName().equals(methodName)) {
                    methodId = i;
                    break;
                }
            }
            writer.write(int.class, methodId);

            Method method = interfaceType.getMethods()[methodId];
            Class<?>[] parametersInfos = method.getParameterTypes();
            for (int i = 0; i <= parametersInfos.length - 1; i = i + 1)
                writer.write(parametersInfos[i], args[i]);

            output.flush();

            if((Byte) reader.read(byte.class) == 0)
            	throw new Exception((String) reader.read(String.class));

            else
            	rslt = reader.read(method.getReturnType());

            reader.close();
            writer.close();

        } catch (SecurityException ex) {
            Logger.getLogger(ARSClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method invoke


    private void declareService(Class interfaceType, String implementationClassName) throws IOException, NoSuchFieldException
    {

        if (!servicesIds.containsKey(interfaceType))
            servicesIds.put(interfaceType, new HashMap<String, Integer>());

        if (!servicesIds.get(interfaceType).containsKey(implementationClassName))
        {
            servicesIds.get(interfaceType).put(implementationClassName, freeServiceId);
            freeServiceId = freeServiceId + 1;
        }

        writer.write(byte.class, ARSServer.DECLARE_SERVICE);
        writer.write(String.class, interfaceType.getName());
        writeIndex(servicesIds.get(interfaceType).get(implementationClassName));

        output.flush();

    }// END Method declareService


    public void writeIndex(long index) throws IOException
    {

        if(index > Short.MAX_VALUE) writer.write(int.class, (int)index);
        else if(index > Byte.MAX_VALUE) writer.write(short.class, (short)index);
        else writer.write(byte.class, (byte)index);

    }// END WriteIndex
    

}// END AstennClient
