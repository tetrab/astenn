/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.protocol.aro;

import org.lestr.astenn.plugin.IServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import org.lestr.genver.data.serialization.Reader;
import org.lestr.genver.data.serialization.Writer;

/**
 *
 * @author pibonnin
 */
public class AROServer implements IServer {


    public static final byte OBJECT_DECLARATION = 0;


    public static final byte OBJECT_INSTANCIATION = 1;


    public static final String NAME = "Native SOAP Server";


    private Thread serverProcess;


    private ServerSocket serverSocket;


    private boolean stop;


    private int port;


    public AROServer() {

        serverSocket = null;
        port = 4597;

        serverProcess = new Thread() {


            @Override
            public void run() {

                while (!stop) {
                    try {

                        final Socket socket = serverSocket.accept();

                        if (socket.isConnected()) {
                            new Thread() {


                                @Override
                                public void run() {

                                    try {

                                        ClientContext context = new ClientContext(socket);

                                        while (true) {

                                            switch ((Byte) context.reader.read(byte.class)) {

                                                case OBJECT_DECLARATION:
                                                    objectDeclaration(context);
                                                    break;
                                                case OBJECT_INSTANCIATION:
                                                    objectInstanciation(context);
                                                    break;

                                            }

                                        }

                                    } catch (Exception e) {
                                    }

                                }// END Method run


                            }.start();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }// END Method run


        };

    }// END Constructor


    @Override
    public String getName() {

        return NAME;

    }// END Method getName


    /**
     * @return Port TCP utilis� par le serveur.
     */
    public int getPort() {

        return port;

    }// END Method getPort


    /**
     * @param Port TCP utilis� par le serveur.
     */
    public void setPort(int port) {

        this.port = port;

    }// END Method setPort


    @Override
    public void start() throws Exception {

        serverSocket = new ServerSocket(getPort());
        serverProcess.start();

    }// END Method start


    @Override
    public void stop() throws Exception {

        stop = true;
        serverSocket.close();
        serverSocket = null;

    }// END Method stop


    private void objectDeclaration(ClientContext context) throws IOException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
    }// END Method objectDeclaration


    private void objectInstanciation(ClientContext context) throws IOException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException {
    }// END Method objectInstanciation


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

