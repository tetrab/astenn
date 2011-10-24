/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lestr.astenn.configuration;

import org.lestr.astenn.plugin.IPersistenceDriver;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pibonnin
 */
public class AnnotationPersistenceDriver implements IPersistenceDriver {


    @Override
    public void addPluginInterface(String pluginInterfaceName) {
    }


    @Override
    public void removePluginInterface(String pluginInterfaceName) {
    }


    @Override
    public void addPluginImplementation(String pluginInterfaceName,
                                        String pluginImplementationAddress) {
    }


    @Override
    public void removePluginImplementation(String pluginInterfaceName,
                                           String pluginImplementationAddress) {
    }


    @Override
    public boolean existPluginInterface(String pluginInterfaceName) {

        return search().containsKey(pluginInterfaceName);

    }// END Method existPluginInterface


    @Override
    public boolean existPluginImplementation(String pluginInterfaceName,
                                             String pluginImplementationAddress) {

        Map<String, Collection<String>> pluginsAndInterfaces = search();

        return existPluginInterface(pluginInterfaceName)
               && pluginsAndInterfaces.get(pluginInterfaceName).contains(pluginImplementationAddress);

    }// END Method existPluginImplementation


    @Override
    public Iterable<String> getPluginInterfacesNames() {

        return search().keySet();

    }// END Method getPluginInterfacesNames


    @Override
    public Iterable<String> getPluginImplementationsAddresses(String pluginInterfaceName) {

        Map<String, Collection<String>> pluginsAndInterfaces = search();

        return pluginsAndInterfaces.containsKey(pluginInterfaceName)
               ? pluginsAndInterfaces.get(pluginInterfaceName)
               : new ArrayList<String>();

    }// END Method getPluginImplementationsURIs


    private Map<String, Collection<String>> search() {

        Map<String, Collection<String>> rslt = new HashMap<String, Collection<String>>();

        try {

            ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

            for (String entry : System.getProperty("java.class.path").split(System.getProperty("path.separator")))
                classes.addAll(entry.endsWith(".jar") ? processJar(entry) : processDirectory(entry));

            for (Class<?> klass : classes) {

                String interfaceName = klass.getAnnotation(Plugin.class).value().getName();

                if (!rslt.containsKey(interfaceName))
                    rslt.put(interfaceName, new ArrayList<String>());

                rslt.get(interfaceName).add("local:" + klass.getName());

            }

        } catch (Exception ex) {
            Logger.getLogger(AnnotationPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method search


    private Collection<Class<?>> processDirectory(String directory) {

        Collection<Class<?>> rslt = new ArrayList<Class<?>>();

        FilenameFilter classFilter = new FilenameFilter() {


            @Override
            public boolean accept(File dir,
                                  String name) {

                return name.endsWith(".class") && !new File(name).isDirectory();

            }


        };

        FilenameFilter subPackageFilter = new FilenameFilter() {


            @Override
            public boolean accept(File dir,
                                  String name) {

                return name.matches("\\w+") && new File(dir.getAbsolutePath() + File.separator + name).isDirectory();

            }


        };

        Stack<String> directories = new Stack<String>();
        Stack<String> packagesNames = new Stack<String>();

        directories.push(directory);
        packagesNames.push("");

        while (!directories.empty()) {

            String currentDirectory = directories.pop();
            String currentPackageName = packagesNames.pop();

            String[] classes = new File(currentDirectory).list(classFilter);

            if (classes != null)
                for (String classFileName : classes) {

                    String className = classFileName.substring(classFileName.lastIndexOf('/') + 1);
                    className = className.substring(0, className.length() - ".class".length());
                    className = currentPackageName + (currentPackageName.equals("") ? "" : ".") + className;

                    try {

                        Class<?> klass = Class.forName(className);

                        if (klass.getAnnotation(Plugin.class) != null)
                            rslt.add(klass);

                    } catch (NoClassDefFoundError ex) {
                    } catch (Exception ex) {
                    }

                }

            String[] subPackages = new File(currentDirectory).list(subPackageFilter);

            if (subPackages != null)
                for (String subPackage : subPackages) {

                    directories.push(currentDirectory + File.separator + subPackage);

                    packagesNames.push(currentPackageName
                                       + (currentPackageName.equals("") ? "" : ".")
                                       + subPackage.substring(subPackage.lastIndexOf("/") + 1));

                }

        }

        return rslt;

    }// END Method processDirectory


    private Collection<Class<?>> processJar(String jar) {

        ArrayList<Class<?>> rslt = new ArrayList<Class<?>>();

        try {

            for (Enumeration<JarEntry> entries = new JarFile(jar).entries(); entries.hasMoreElements();) {

                JarEntry element = entries.nextElement();

                if (element.getName().endsWith(".class")) {

                    String className = element.getName();
                    className = className.substring(0, className.length() - ".class".length());
                    className = className.replaceAll("/", ".");

                    try {

                        Class<?> klass = Class.forName(className);

                        if (klass.getAnnotation(Plugin.class) != null)
                            rslt.add(klass);

                    } catch (NoClassDefFoundError ex) {
                    } catch (Exception ex) {
                    }

                }

            }

        } catch (IOException ex) {
            Logger.getLogger(AnnotationPersistenceDriver.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rslt;

    }// END Method processJar


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Plugin {


        Class<?> value();


    }// END Annotation Plugin


}// END Class EmbbedXMLDocumentPersistenceDriver


