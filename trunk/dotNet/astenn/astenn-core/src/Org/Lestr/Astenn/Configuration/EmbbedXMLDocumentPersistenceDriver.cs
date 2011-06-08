/*
 * Crée par SharpDevelop.
 * Utilisateur: Choupis
 * Date: 30/04/2010
 * Heure: 22:33
 * 
 * Pour changer ce modèle utiliser Outils | Options | Codage | Editer les en-têtes standards.
 */
using System;
using System.IO;
using System.Text;
using System.Xml;
using System.Collections.Generic;
using System.Reflection;
using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Configuration
{
	
	
	public class EmbbedXMLDocumentPersistenceDriver : IPersistenceDriver {


	    private CompositePersistenceDriver cachedPersistenceDriver;
	
	
	    public EmbbedXMLDocumentPersistenceDriver()
	    {
            cachedPersistenceDriver = new CompositePersistenceDriver();
            
            foreach(Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
                foreach(string resourceName in assembly.GetManifestResourceNames())
                if(resourceName.EndsWith(".astenn.xml"))
                {
                
                    Stream s = assembly.GetManifestResourceStream(resourceName);

                    byte[] data = new byte[s.Length];
                    s.Read(data, 0, data.Length);
                    s.Close();

                    XmlDocument document = new XmlDocument();
                    document.LoadXml(Encoding.Default.GetString(data));

                    cachedPersistenceDriver.ReadOnlyPersistenceDrivers.Add(new XMLDocumentPersistenceDriver(document));

                }
	
	    }// END Constructor
	
	
	    public void AddPluginInterface(string pluginInterfaceName) {
	    }// END Method AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) {
	    }// END Method RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) {

            return cachedPersistenceDriver.ExistPluginInterface(pluginInterfaceName);
	
	    }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            cachedPersistenceDriver.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

	    }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            cachedPersistenceDriver.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);

	    }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            return cachedPersistenceDriver.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress);

	    }// END Method ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() 
        {

            return cachedPersistenceDriver.GetPluginInterfacesNames();

	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {

            return cachedPersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceName);

	    }// END Method GetPluginImplementationsNames
	    

	}// END Class EmbbedXMLDocumentPersistenceDriver
	
	
}// END Namespace Astenn.Configuration
