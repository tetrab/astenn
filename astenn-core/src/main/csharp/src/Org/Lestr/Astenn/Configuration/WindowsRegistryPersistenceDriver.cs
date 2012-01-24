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
using Microsoft.Win32;
using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Configuration
{
	
	
	public class WindowsRegistryPersistenceDriver : IPersistenceDriver {
	
	
	    private RegistryKey key;
	
	
	    public WindowsRegistryPersistenceDriver()
	    {

	    	try { key = Registry.CurrentUser.OpenSubKey(@"SOFTWARE\Astenn", true); } catch (System.Exception) { }
	    	
	    }// END Constructor
	    
	    
		public RegistryKey Key {
	    	
			get { return key; }
			set { key = value; }
			
		}// END Property RegistryKey
	
	
	    public void AddPluginInterface(string pluginInterfaceName) {
	
	    	key.SetValue(pluginInterfaceName, new string[0], RegistryValueKind.MultiString);
	
	    }// END Method AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) {
	
	        key.DeleteValue(pluginInterfaceName);
	
	    }// END Method RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) {
	
	    	return new List<string>(key.GetValueNames()).Contains(pluginInterfaceName);
	
	    }// END Method ExistPluginInterface
	
	
	    public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) {
	
	    	List<string> implementations = new List<string>((string[])key.GetValue(pluginInterfaceName));
	    	implementations.Add(pluginImplementationAddress.ToString());
	    	key.SetValue(pluginInterfaceName, implementations.ToArray(), RegistryValueKind.MultiString);
	
	    }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	List<string> implementations = new List<string>((string[])key.GetValue(pluginInterfaceName));
	    	implementations.Remove(pluginImplementationAddress.ToString());
	    	key.SetValue(pluginInterfaceName, implementations.ToArray(), RegistryValueKind.MultiString);
	
	    }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	    	return new List<string>((string[])key.GetValue(pluginInterfaceName)).Contains(pluginImplementationAddress.ToString());
	
	    }// END Method ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() {
	
	    	return key.GetValueNames();
	
	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {

            List<string> rslt = new List<string>();
	    	
	    	foreach(string pluginImplementationAddress in (string[])key.GetValue(pluginInterfaceName))
	    		rslt.Add(pluginImplementationAddress);
	    	
	    	return rslt;
	
	    }// END Method GetPluginImplementationsNames
	    

	}// END Classe RAMPersistenceDriver
	
	
}// END Namespace Astenn.Configuration
