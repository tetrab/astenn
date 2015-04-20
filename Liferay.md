# Introduction #
Astenn propose une intégration à Liferay permettant de persister dans les propriétés d'un site web Liferay des extensions qui lui sont spécifiques. Vous pouvez ajouter cette intégration aux dépendances de votre projet à l'aide de [Maven](Maven.md).

# Exemple #
## Enregistrement d'une extension pour un certain site Liferay ##
```
// Sélection du site Liferay cible à l'aide de son identifiant
LiferayPersistenceDriver.getSingleton().openWebsite(10568);

// Enregistrement de l'extension pour le site Liferay cible
PluginsManager.getSingleton().registerPlugin(IMonExtension.class, 
                                             MonExtension.class, 
                                             LiferayPersistenceDriver.getSingleton());

// Fermeture du site Liferay cible
LiferayPersistenceDriver.getSingleton().closeWebsite();
```
## Obtention d'extensions ##
```
// Sélection du site Liferay cible à l'aide de son identifiant
LiferayPersistenceDriver.getSingleton().openWebsite(10568);

// Obtention d'extensions, dont celles du site Liferay sélectionné
Iterable<IMonExtension> plugins = PluginsManager.getSingleton().getRegisterPlugins(IMonExtension.class);

// Fermeture du site Liferay cible
LiferayPersistenceDriver.getSingleton().closeWebsite();
```