# Introduction #
Astenn propose une intégration à SharePoint 2010 permettant de persister dans les propriétés d'une ferme, d'une application web ou d'un site web SharePoint des extensions qui lui sont spécifiques.

# Exemple #
## Enregistrement d'une extension pour une ferme SharePoint ##
```
// Sélection de la ferme
SharePointPersistenceDriver.Singleton.OpenSPFarm();

// Enregistrement de l'extension pour la ferme SharePoint
PluginsManager.Singleton.RegisterPlugin(typeof(IMonExtension), 
                                        typeof(MonExtension),
                                        SharePointPersistenceDriver.Singleton);

// Fermeture de la ferme
SharePointPersistenceDriver.Singleton.Close();
```
## Obtention d'extensions ##
```
// Sélection de la ferme
SharePointPersistenceDriver.Singleton.OpenSPFarm();

// Obtention d'extensions, dont celles de la ferme
IEnumerable<IMonExtension> plugins = PluginsManager.Singleton.gGtRegisterPlugins(typeof(IMonExtension));

// Fermeture de la ferme
SharePointPersistenceDriver.Singleton.Close();
```