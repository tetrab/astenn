/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.protocol.ars;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import org.lestr.astenn.plugin.IServer;
import org.lestr.genver.data.serialization.Reader;
import org.lestr.genver.data.serialization.Writer;
import org.lestr.genver.network.TCPServer;

/**
 *
 * @author pibonnin
 */
public class ARSServer extends TCPServer implements IServer {


    public static final byte DECLARE_SERVICE = 0;
    public static final byte INVOKE_SERVICE_METHOD = 1;
    public static final String NAME = "ARS Server";


    public ARSServer() {

        setPort(4596);

    }// END Constructor


    @Override
    public String getName() {

        return NAME;

    }// END Method getName


    @Override
    protected void processClient(Socket socket) {

        try {

            ClientContext context = new ClientContext(socket);

            while (true) {

                context.reader.open(context.input);
                context.writer.open(context.output);

                switch ((Byte) context.reader.read(byte.class)) {

                    case DECLARE_SERVICE:
                        declareService(context);
                        break;
                    case INVOKE_SERVICE_METHOD:
                        invokeServiceMethod(context);
                        break;

                }

                context.writer.close();
                context.reader.close();

            }

        } catch (Exception e) { }

    }// END Method processClient


    private void invokeServiceMethod(ClientContext context) throws IOException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException {

        int serviceId = readIndex(context, context.lastServiceId);

        int methodId = (Integer) context.reader.read(int.class);

        Class<?> interfaceType = context.servicesTypes.get(serviceId)[0];
        Class<?> implementationType = context.servicesTypes.get(serviceId)[1];
        Method methodInfo = interfaceType.getMethods()[methodId];

        Object service = implementationType.getConstructor(new Class<?>[0]).newInstance(new Object[0]);

        ArrayList<Object> parameters = new ArrayList<Object>();
        Class<?>[] parametersTypes = methodInfo.getParameterTypes();
        for (int i = 0; i <= parametersTypes.length - 1; i = i + 1) {
            parameters.add(context.reader.read(parametersTypes[i]));
        }

        Object rslt = null;

        try {
            rslt = methodInfo.invoke(service, parameters.toArray());
        } catch (Exception e) {

            context.writer.write(byte.class, 0);
            context.writer.write(String.class, e.getMessage());
            context.output.flush();

            return;

        }

        context.writer.write(byte.class, 1);
        context.writer.write(methodInfo.getReturnType(), rslt);
        context.output.flush();

    }// END Method InvokeServiceMethod


    private void declareService(ClientContext context) throws IOException, ClassNotFoundException {

        String interfaceTypeName = (String) context.reader.read(String.class);
        String implementationTypeName = (String) context.reader.read(String.class);

        int serviceId = readIndex(context, context.lastServiceId);
        context.lastServiceId = serviceId;

        Class<?> interfaceType = getClass().getClassLoader().loadClass(interfaceTypeName);
        Class<?> implementationType = getClass().getClassLoader().loadClass(implementationTypeName);

        context.servicesTypes.put(serviceId, new Class<?>[]{interfaceType, implementationType});

    }// END Method DeclareService


    private int readIndex(ClientContext context,
                          long lastIndex) throws IOException {

        if (lastIndex >= Short.MAX_VALUE) {
            return (Integer) context.reader.read(Integer.class);
        } else if (lastIndex >= Byte.MAX_VALUE) {
            return (Short) context.reader.read(Short.class);
        } else {
            return (Byte) context.reader.read(Byte.class);
        }

    }// END ReadIndex


    public String getPluginRemoteAddress(Class<?> pluginInterfaceClass,
                                         Class<?> pluginImplementationClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    private class ClientContext {


        Reader reader;


        Writer writer;


        InputStream input;


        OutputStream output;


        HashMap<Integer, Class<?>[]> servicesTypes;


        long lastServiceId;


        ClientContext(Socket socket) throws IOException {

            servicesTypes = new HashMap<Integer, Class<?>[]>();
            lastServiceId = 0;
            reader = new Reader();
            writer = new Writer();
            input = socket.getInputStream();
            output = socket.getOutputStream();

        }// END Constructor


    }// END Class Client ClientContext

    
}// END Class AstennServer

