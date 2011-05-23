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
using System.Xml;
using Org.Lestr.Astenn.Plugin;

namespace Org.Lestr.Astenn.Configuration
{
	
	
	public class XMLDocumentPersistenceDriver : IPersistenceDriver {
	
	
	    private XmlDocument document;
	
	
	    public XMLDocumentPersistenceDriver()
	    {
	
	    	document = new XmlDocument();
	    	
	    	document.AppendChild(document.CreateElement("astenn"));
	
	    }// END Constructor


	    public XMLDocumentPersistenceDriver(XmlDocument document)
	    {
	        
	        this.document = document;
	
	    }// END Constructor
	    
	    
		public XmlDocument Document 
		{
	    	
			get { return document; }
			set { document = value; }
			
		}// END Property Document
	
	
	    public void AddPluginInterface(string pluginInterfaceName) 
	    {
			
	    	XmlElement interfaceElement = Document.CreateElement("plugin-interface");
	    	interfaceElement.SetAttribute("class-name", pluginInterfaceName);
	    	
	    	document.DocumentElement.AppendChild(interfaceElement);
	
	    }// END Method AddPluginInterface
	
	
	    public void RemovePluginInterface(string pluginInterfaceName) 
	    {
	
	    	document.DocumentElement.RemoveChild(GetPluginInterfaceElement(pluginInterfaceName));
	
	    }// END Method RemovePluginInterface
	
	
	    public bool ExistPluginInterface(string pluginInterfaceName) 
	    {
	
	        return GetPluginInterfaceElement(pluginInterfaceName) != null;
	
	    }// END Method ExistPluginInterface


        public void AddPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) 
	    {
			
	    	XmlElement implementationElement = Document.CreateElement("plugin-implementation");
            implementationElement.SetAttribute("address", pluginImplementationAddress.ToString());
	    	
	    	GetPluginInterfaceElement(pluginInterfaceName).AppendChild(implementationElement);
	
	    }// END Method AddPluginImplementation


        public void RemovePluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) 
	    {
	
	    	XmlElement pluginImplementationElement = GetPluginImplementationElement(pluginInterfaceName, pluginImplementationAddress);
	    	pluginImplementationElement.ParentNode.RemoveChild(pluginImplementationElement);
	
	    }// END Method RemovePluginImplementation


        public bool ExistPluginImplementation(string pluginInterfaceName, string pluginImplementationAddress) 
	    {
	
	    	return GetPluginImplementationElement(pluginInterfaceName, pluginImplementationAddress) != null;
	
	    }// END Method ExistPluginImplementation
	
	
	    public IEnumerable<string> GetPluginInterfacesNames() 
	    {
	
	    	List<string> rslt = new List<string>();
	    	
	    	foreach(XmlNode node in Document.DocumentElement.ChildNodes)
	    		if(node.NodeType == XmlNodeType.Element &&
	    		   node.Name.Equals("plugin-interface") &&
	    		   ((XmlElement)node).HasAttribute("class-name"))
	    			rslt.Add(((XmlElement)node).GetAttribute("class-name"));
	    	
	    	return rslt;
	
	    }// END Method GetPluginImplementationsNames


        public IEnumerable<string> GetPluginImplementationsAddresses(string pluginInterfaceName) 
	    {

            List<string> rslt = new List<string>();
	    	
	    	foreach(XmlNode node in GetPluginInterfaceElement(pluginInterfaceName).ChildNodes)
	    		if(node.NodeType == XmlNodeType.Element &&
	    		   node.Name.Equals("plugin-implementation") &&
                   ((XmlElement)node).HasAttribute("address"))
                    rslt.Add(((XmlElement)node).GetAttribute("address"));
	    	
	    	return rslt;
	
	    }// END Method GetPluginImplementationsURIs
	    
	    
	    private XmlElement GetPluginInterfaceElement(string pluginInterfaceName)
	    {
	    	
	    	foreach(XmlNode node in Document.DocumentElement.ChildNodes)
	    		if(node.NodeType == XmlNodeType.Element &&
	    		   node.Name.Equals("plugin-interface") &&
	    		   ((XmlElement)node).HasAttribute("class-name") &&
	    		   ((XmlElement)node).GetAttribute("class-name").Equals(pluginInterfaceName))
	    			return (XmlElement)node;
	    	
	    	return null;
	    	
	    }// END Method GetPluginInterfaceElement


        private XmlElement GetPluginImplementationElement(string pluginInterfaceName, string pluginImplementationURI)
	    {
	    	
	    	XmlElement pluginInterfaceElement = GetPluginInterfaceElement(pluginInterfaceName);
	    	
	    	if(pluginInterfaceElement != null)
		    	foreach(XmlNode node in pluginInterfaceElement.ChildNodes)
		    		if(node.NodeType == XmlNodeType.Element &&
		    		   node.Name.Equals("plugin-implementation") &&
                       ((XmlElement)node).HasAttribute("address") &&
                       ((XmlElement)node).GetAttribute("address").Equals(pluginImplementationURI.ToString()))
		    			return (XmlElement)node;
	    	
	    	return null;
	    	
	    }// END Method GetPluginInterfaceElement
	    

	}// END Classe XMLDocumentPersistenceDriver
	
	
}// END Namespace Astenn.Configuration
