# Introduction #

Les éléments clés du noyau d'Astenn sont les suivants :

  * **Le gestionnaire d'extensions :** point d'entrée d'Astenn, permet l'enregistrement et l'obtention des extensions ;
  * **Les pilotes de persistance :** assurent la persistance des données relatives aux extensions enregistrées ;
  * **Les fournisseurs d'extensions :** permet à Astenn d'obtenir les instances des extensions enregistrées.

# Gestionnaire d'extensions #

Le gestionnaire d'extensions est un singleton permettant l'enregistrement de nouvelles extensions et l'obtention de celles enregistrées (méthodes `registerPlugin`, `unregisterPlugin` et `getRegisteredPlugins`). Il permet également d'accéder et de modifier la configuration d'Astenn (méthode `getConfiguration`).

# Pilotes de persistance #

Les pilotes de persistance (_persistence driver_) sont chargés d'assurer la persistance de la liste des extensions enregistrées. Il s'agit de simples classes Java implémentant l'interface `org.lestr.astenn.configuration.IPersistanceDriver`. Le noyau d'Astenn propose nativement un certain nombre de pilotes de persistance :

  * **Document XML :** document XML dont la provenance doit être programmée (fichier disque...) ;
  * **Document XML embarqué :** fichiers XML `META-INF/astenn.xml` présent dans le _classpath_ de l'application (dans les fichiers `*.jar`...) ;
  * **Annotation Java :** annotation `@Plugin` des classes d'extensions ;
  * **Préférences Java :** persistance à l'aide du système de _préférences_ de Java ;
  * **Registre Windows :** persistance dans la base de registre de Windows ;
  * **Pas de persistance :** les données sont perdues à chaque redémarrage de l'application ;
  * **Pilote composite :** pilote mettant en oeuvre le patron de conception "Composite" pour regrouper tout un ensemble de pilotes de persistance. Un pilote est en lecture/écriture, les autres sont en lecture seule ;
  * **Proprietes :** persistance à l'aide d'un objet `java.util.Properties`.
  * **Cache :** met en cache un autre pilote de persistance.

Astenn utilise un pilote de persistance de type "Pilote composite" comme pilote "racine". Il est possible de modifier l'ensemble des pilotes que ce composite regroupe. Le pilote composite racine d'Astenn peut être obtenu de la manière suivante :

```
CompositePersistenceDriver driver = PluginsManager.getSingleton().getConfiguration().getPersistenceDriver();
```

Par défaut, le pilote de persistance d'Astenn est configuré de la manière suivante :

![http://astenn.googlecode.com/svn/wiki/PilotesParDefaut.png](http://astenn.googlecode.com/svn/wiki/PilotesParDefaut.png)

# Fournisseurs d'extensions #

Les fournisseurs d'extensions sont utilisés par le gestionnaire d'extensions pour obtenir les instances des extensions enregistrées dans les pilotes de persistance. Le gestionnaire d'extension d'Astenn dispose nativement d'un fournisseur d'extensions pour les extensions locales. Le projet [astenn-cxf](SOAP.md) comporte un fournisseur d'extensions distantes s'appuyant sur SOAP. Le projet [astenn-rmi](RMI.md) propose un équivalent pour RMI.

Les fournisseurs d'extensions sont eux-mêmes des extensions Astenn. Ils sont spécifiés par l'interface `org.lestr.astenn.plugin.IPluginsProvider`. Cette interface impose la présence de deux méthodes :

  * **`getScheme` :** retourne la chaîne de caractères utilisée pour identifier les extensions pouvant être obtenues à l'aide du fournisseur d'extensions ;
  * **`getPlugin` :** retourne une instance d'une extension à partir de son interface de spécification et l'adresse de son implémentation.

Pour implémenter votre propre fournisseur d'extensions, il vous est possible de prendre modèle sur le code source des projets [astenn-cxf](http://astenn.googlecode.com/svn/trunk/java/astenn-cxf) ou [astenn-rmi](http://astenn.googlecode.com/svn/trunk/java/astenn-rmi).