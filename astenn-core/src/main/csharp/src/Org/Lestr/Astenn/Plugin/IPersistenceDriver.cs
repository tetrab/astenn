/*
 * Crée par SharpDevelop.
 * Utilisateur: Choupis
 * Date: 30/04/2010
 * Heure: 22:31
 * 
 * Pour changer ce modèle utiliser Outils | Options | Codage | Editer les en-têtes standards.
 */
using System;
using System.Collections.Generic;

namespace Org.Lestr.Astenn.Plugin
{
	
	
	public interface IPersistenceDriver {
	
	
	    void AddPluginInterface(string pluginInterfaceName);
	
	
	    void RemovePluginInterface(string pluginInterfaceName);
	
	
	    bool ExistPluginInterface(string pluginInterfaceName);


        void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress);


        void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress);


        bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress);
	
	
	    IEnumerable<string> GetPluginInterfacesNames();
	
	
	    IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName);
	
	    
	}// END Interface PersistenceDriver
	
		
}// END Namespace Astenn.Configuration
