/*
 * Crée par SharpDevelop.
 * Utilisateur: PIBONNIN
 * Date: 25/05/2010
 * Heure: 15:18
 * 
 * Pour changer ce modèle utiliser Outils | Options | Codage | Editer les en-têtes standards.
 */
using System;

using System.Reflection;

namespace Org.Lestr.Astenn.Plugin
{
	
	
	/// <summary>
	/// Description of IRemotePluginsProvider.
	/// </summary>
	public interface IPluginsProvider
	{
		
		
		string Scheme { get; }


        PluginInterfaceType GetPlugin<PluginInterfaceType>(string pluginImplementationAddress);


    }// END Interface IPluginsProvider


    public class LocalPluginsProvider : IPluginsProvider
    {


        public string Scheme
        {

            get { return "local"; }

        }// END Property Scheme


        public PluginInterfaceType GetPlugin<PluginInterfaceType>(string pluginImplementationAddress)
        {

            string pluginImplementationAssemblyQualifiedClassName = pluginImplementationAddress.Substring(Scheme.Length + 1);
            string assemblyName = pluginImplementationAssemblyQualifiedClassName.Substring(pluginImplementationAssemblyQualifiedClassName.IndexOf(',') + 1);
            string typeName = pluginImplementationAssemblyQualifiedClassName.Substring(0, pluginImplementationAssemblyQualifiedClassName.IndexOf(','));
            Type pluginImplementationClass = Assembly.Load(assemblyName).GetType(typeName);
            object pluginImplementationInstance = pluginImplementationClass.GetConstructor(new Type[0]).Invoke(new object[0]);

            return (PluginInterfaceType)pluginImplementationInstance;

        }// END Method PluginInterfaceType


    }// END Class LocalPluginsProvider
	
	
}// END namespace Astenn.Protocol
