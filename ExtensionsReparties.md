# Introduction #

Astenn vous permet de faire appel, depuis vos programmes, à des extensions hébergées localement et/ou dans une autre JVM. Les protocoles de communications actuellement supportés sont :

  * [SOAP](SOAP.md) (en Java, utilisation de la librairie CXF. En .NET, utilisation de WCF)
  * [RMI](RMI.md)
  * .NET Remoting

# Exemple simple #
## Définition d'une extension ##
```
package monprojet;

public interface IMonExtension {

   void ditCoucou();

}
```
## Sur un serveur A ##
## Implémentation A de l'extension ##
```
package monprojet;

public interface MonExtensionImplA implements IMonExtension {

   public void ditCoucou() {
   
      System.out.println("Coucou ! Je suis sur le serveur A !");

   }

}
```
## Enregistrement par code de l'implémentation A ##
```
PluginsManager.getSingleton().registerPlugin(IMonExtension.class, MonExtensionImplA.class);
```
## Démarrage d'un serveur SOAP autonome ##
Il est également possible de s'appuyer un conteneur de servlets (Tomcat, Jetty...).
```
SOAPServer server = new SOAPServer();
server.start();
```
## Exposition de l'implémentation A ##
Le code ci-dessous illustre comment rendre disponible à des applications distantes une extension enregistrée localement. Astenn peut être configuré pour que toutes les extensions enregistrées soient exposées.
```
IPermissionsManager p = PluginsManager.getSingleton().getConfiguration().getPermissionsManager();

p.exposeLocalPlugin(IMonExtension.class, MonExtensionImplA.class);
```
## Sur un serveur B ##
## Implémentation B de l'extension ##
```
package org.monorganisation.monprojet;

public interface MonExtensionImplB implements IMonExtension {

   public void ditCoucou() {
   
      System.out.println("Coucou ! Je suis sur le serveur B !");

   }

}
```
## Enregistrement par code de l'implémentation B ##
```
PluginsManager.getSingleton().registerPlugin(IMonExtension.class, MonExtensionImplB.class);
```
## Enregistrement par code de l'implémentation A du serveur A ##
```
String adresse = "soap:http://serveur-a/monprojet.IMonExtension/monprojet.MonExtensionImplA";

PluginsManager.getSingleton().registerPlugin(IMonExtension.class, adresse);
```
## Obtention des extensions enregistrées ##
La ligne de code ci-dessous retourne l'ensemble des extensions de type "IMonExtension", qu'elles soient présentes localement ou sur le serveur A.
```
Iterable<IMonExtension> extensions = PluginsManager.getSingleton().getRegisteredPlugins(IMonExtension.class);
```