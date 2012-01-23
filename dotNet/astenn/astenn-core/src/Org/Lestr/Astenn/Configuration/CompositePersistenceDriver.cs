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
	
	
	public class CompositePersistenceDriver : IPersistenceDriver {
	
	
	
	    private IPersistenceDriver readWritePersistenceDriver;
	    private List<IPersistenceDriver> readOnlyPersistenceDrivers;
	
	
	    public CompositePersistenceDriver()
	    {
	
	        readWritePersistenceDriver = new RAMPersistenceDriver();
	        readOnlyPersistenceDrivers = new List<IPersistenceDriver>();
	
	    }// END Constructor
	
	
	    public CompositePersistenceDriver(IPersistenceDriver readWritePersistenceDriver, IEnumerable<IPersistenceDriver> readOnlyPersistenceDrivers)
	    {

            this.readWritePersistenceDriver = readWritePersistenceDriver;
            this.readOnlyPersistenceDrivers = new List<IPersistenceDriver>(readOnlyPersistenceDrivers);
	
	        foreach(IPersistenceDriver driver in readOnlyPersistenceDrivers)
	            this.readOnlyPersistenceDrivers.Add(driver);
	
	    }// END Constructor
	    
	    
		public List<IPersistenceDriver> ReadOnlyPersistenceDrivers {
	    	
			get { return readOnlyPersistenceDrivers; }
			
		}// END Property ReadOnlyPersistenceDrivers
	    
	    
		public IPersistenceDriver ReadWritePersistenceDriver {
	    	
			get { return readWritePersistenceDriver; }
			set { readWritePersistenceDriver = value; }
			
		}// END Property ReadWritePersistenceDriver
	
	
	    public void AddPluginInterface(string pluginInterfaceName) {
	
	        readWritePersistenceDriver.AddPluginInterface(pluginInterfaceName);
	
	    }// END Property AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) {
	
			if(readWritePersistenceDriver.ExistPluginInterface(pluginInterfaceName))
				readWritePersistenceDriver.RemovePluginInterface(pluginInterfaceName);
	
	    }// END Property RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) {
	
	        if(readWritePersistenceDriver.ExistPluginInterface(pluginInterfaceName))
	            return true;
	
	        foreach(IPersistenceDriver driver in readOnlyPersistenceDrivers)
	            if(driver.ExistPluginInterface(pluginInterfaceName))
	                return true;
	
	        return false;
	
	    }// END Property ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {
	
	        readWritePersistenceDriver.AddPluginImplementation(pluginInterfaceName, pluginImplementationAddress);
	
	    }// END Property AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

			if (readWritePersistenceDriver.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
				readWritePersistenceDriver.RemovePluginImplementation(pluginInterfaceName, pluginImplementationAddress);
	
	    }// END Property RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress)
        {

            if (readWritePersistenceDriver.ExistPluginInterface(pluginInterfaceName)
                && readWritePersistenceDriver.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
	            return true;
	
	        foreach(IPersistenceDriver driver in readOnlyPersistenceDrivers)
	            if(driver.ExistPluginInterface(pluginInterfaceName)
                    && driver.ExistPluginImplementation(pluginInterfaceName, pluginImplementationAddress))
	                return true;
	        
	        return false;
	
	    }// END Property ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() {
	
	        List<String> rslt = new List<String>();
	
	        foreach(string interfaceName in readWritePersistenceDriver.GetPluginInterfacesNames())
	            rslt.Add(interfaceName);
	
	        foreach(IPersistenceDriver driver in readOnlyPersistenceDrivers)
	            foreach(string interfaceName in driver.GetPluginInterfacesNames())
	                if(!rslt.Contains(interfaceName))
	                    rslt.Add(interfaceName);
	
	        return rslt;
	
	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName)
        {

            List<string> rslt = new List<string>();

            if (readWritePersistenceDriver.ExistPluginInterface(pluginInterfaceName))
                foreach (string implementation in readWritePersistenceDriver.GetPluginImplementationsAddresses(pluginInterfaceName))
	                rslt.Add(implementation);
	
	        foreach(IPersistenceDriver driver in readOnlyPersistenceDrivers)
                if (driver.ExistPluginInterface(pluginInterfaceName))
                    foreach (string implementation in driver.GetPluginImplementationsAddresses(pluginInterfaceName))
	                    if(!rslt.Contains(implementation))
	                        rslt.Add(implementation);
	
	        return rslt;
	
	    }// END Property GetPluginImplementationsNames
	
	
	}// END Class CompositePersistenceDriver
	
	
}// END Namespace Astenn.Configuration
