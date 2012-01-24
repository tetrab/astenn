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
	
	
	public class CachePersistenceDriver : IPersistenceDriver {


        private Dictionary<string, List<string>> plugins;
	    private IPersistenceDriver cachedPersistenceDriver;
	
	
	    public CachePersistenceDriver()
	    {

            plugins = new Dictionary<string, List<string>>();
	        cachedPersistenceDriver = null;
	
	    }// END Constructor


	    public CachePersistenceDriver(IPersistenceDriver cachedPersistenceDriver)
	    {
	
	        this.cachedPersistenceDriver = cachedPersistenceDriver;
	
	    }// END Constructor
	    
	    
		public IPersistenceDriver CachedPersistenceDriver {
	    	
			get { return cachedPersistenceDriver; }
			
			set 
			{ 
				
				if(cachedPersistenceDriver != value)
				{
					
					cachedPersistenceDriver = value; 
					ReloadCache();
					
				}
				
			}
			
		}// END Property CachedPersistenceDriver
	    
	    
	    public void ReloadCache()
	    {

            plugins = new Dictionary<string, List<string>>();
			
			foreach(string pluginInterfaceName in cachedPersistenceDriver.GetPluginInterfacesNames())
			{
				
				plugins.Add(pluginInterfaceName, new List<string>());
				
				foreach(string pluginImplementationAddress in cachedPersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceName))
                    plugins[pluginInterfaceName].Add(pluginImplementationAddress);
				
			}
	    	
	    }// END Method ReloadCache
	
	
	    public void AddPluginInterface(string pluginInterfaceName) {
	
	    	CachedPersistenceDriver.AddPluginInterface(pluginInterfaceName);
	        plugins.Add(pluginInterfaceName, new List<string>());
	
	    }// END Method AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) {
	
	    	CachedPersistenceDriver.RemovePluginInterface(pluginInterfaceName);
	        plugins.Remove(pluginInterfaceName);
	
	    }// END Method RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) {
	
	        return plugins.ContainsKey(pluginInterfaceName);
	
	    }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	CachedPersistenceDriver.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);
	    	plugins[pluginInterfaceName].Add(pluginImplementationAddress);
	
	    }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	CachedPersistenceDriver.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);
	    	plugins[pluginInterfaceName].Remove(pluginImplementationAddress);
	
	    }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	return plugins[pluginInterfaceName].Contains(pluginImplementationAddress);
	
	    }// END Method ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() {
	
	    	return plugins.Keys;
	
	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {
	
	    	return plugins[pluginInterfaceName];
	
	    }// END Method GetPluginImplementationsNames
	    

	}// END Class CachePersistenceDriver
	
	
}// END Namespace Astenn.Configuration
