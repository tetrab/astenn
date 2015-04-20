# Introduction #

Astenn est un outil open-source léger facilitant le développement d'applications extensibles, réparties et multi-langages (implémentations Java et .NET disponibles). Astenn permet :

  * Une définition simple de points d'extensions dans les applications ;
  * Une collaboration entre [extensions réparties](ExtensionsReparties.md) sur plusieurs machines via [SOAP](SOAP.md), [RMI](RMI.md) ou .NET Remoting.

Astenn est une solution simple et peu intrusive. Son noyau est petit et ne comporte aucune dépendances. Seule une JRE et/ou un environnement .NET est nécessaire à son fonctionnement. Son API a été conçue de manière à favoriser une prise en main rapide. Cependant, et contrairement à d'autres cadriciels sachant répondre de manière plus complète mais plus lourde à un besoin analogue (par exemple, OSGi), il n'apporte pas de réponse normalisée aux problématiques de gestion du cycle de vie et du conditionnement des composants logiciels.

Astenn propose les fonctionnalités avancées suivantes :

  * [Bus d'extensions](Bus.md)
  * [Intégration à Liferay](Liferay.md)
  * [Intégration à SharePoint](SharePoint.md)

**Vous pouvez ajouter Astenn aux dépendances de votre projet grâce à [Maven](Maven.md)**.

Vous trouverez sur le Wiki du projet un document présentant succinctement l'[architecture](Architecture.md) logicielle d'Astenn.

Autre rubriques :

  * [Historique des versions](Historique.md)
  * [Feuille de route](Route.md)


# Exemple simple #
## Définition d'un point d'extension ##
```
package org.monorganisation.monprojet;

public interface IMonExtension {

   void ditCoucou();

}
```
## Implémentation d'une extension ##
```
package org.monorganisation.monprojet;

public interface MonExtensionImpl implements IMonExtension {

   public void ditCoucou() {
   
      System.out.println("Coucou !");

   }

}
```
## Enregistrement d'une extension ##
Astenn propose nativement un certain nombre de mécanismes pour enregistrer les extensions, mais **vous êtes libre d'en définir d'autres** (sauvegarde en base...).
### Solution 1 : par code ###
```
PluginsManager.getSingleton().registerPlugin(IMonExtension.class, MonExtensionImpl.class);
```
### Solution 2 : par XML ###
```
<?xml version="1.0" encoding="windows-1252"?>
<astenn>

    <plugin-interface class-name="org.monorganisation.monprojet.IMonExtension">
        <plugin-implementation address="local:org.monorganisation.monprojet.MonExtensionImpl" />
    </plugin-interface>

</astenn>
```
### Solution 3 : par annotation ###
```
package org.monorganisation.monprojet;

@Plugin(IMonExtension.class)
public interface MonExtensionImpl implements IMonExtension {

   public void ditCoucou() {
   
      System.out.println("Coucou !");

   }

}
```
## Obtention des extensions enregistrées ##
```
Iterable<IMonExtension> extensions = PluginsManager.getSingleton().getRegisteredPlugins(IMonExtension.class);
```