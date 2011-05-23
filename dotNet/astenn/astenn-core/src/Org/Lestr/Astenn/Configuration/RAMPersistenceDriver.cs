/*
 * Crée par SharpDevelop.
 * Utilisateur: Choupis
 * Date: 30/04/2010
 * Heure: 22:33
 * 
 * Pour changer ce modèle utiliser Outils | Options | Codage | Editer les en-têtes standards.
 */
using System;
using System.Collections.Generic;
using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Configuration
{
	
	
	public class RAMPersistenceDriver : IPersistenceDriver {


        private Dictionary<string, List<string>> plugins;
	
	
	    public RAMPersistenceDriver()
	    {

            plugins = new Dictionary<string, List<string>>();
	
	    }// END Constructor
	
	
	    public void AddPluginInterface(string pluginInterfaceName) {

            plugins.Add(pluginInterfaceName, new List<string>());
	
	    }// END Method AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) {
	
	        plugins.Remove(pluginInterfaceName);
	
	    }// END Method RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) {
	
	        return plugins.ContainsKey(pluginInterfaceName);
	
	    }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (!ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
                plugins[pluginInterfaceName].Add(pluginImplementationAddress);
	
	    }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
	    	    plugins[pluginInterfaceName].Remove(pluginImplementationAddress);
	
	    }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	return ExistPluginInterface(pluginInterfaceName) && plugins[pluginInterfaceName].Contains(pluginImplementationAddress);
	
	    }// END Method ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() {
	
	    	return plugins.Keys;
	
	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {
	
	    	return plugins.ContainsKey(pluginInterfaceName) ? plugins[pluginInterfaceName] : new List<string>();
	
	    }// END Method GetPluginImplementationsNames
	    

	}// END Classe RAMPersistenceDriver
	
	
}// END Namespace Astenn.Configuration
