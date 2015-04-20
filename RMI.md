# Introduction #

Astenn permet la création d'[extensions réparties](ExtensionsReparties.md) à l'aide du protocole RMI. Une dépendance à "astenn-rmi" peut être ajoutée à votre projet grâce à [Maven](Maven.md).

# Format des adresses #
Les extensions exposées à l'aide d'astenn-cxf dispose d'une adresse de la forme : `rmi:<mon_serveur>:<mon_port>/<classe_de_l_interface>/<classe_de_l_implementation>`. Par exemple : `rmi:localhost:1069/monprojet.IMonExtension/monprojet.MonExtensionImpl`.

# Contraintes sur les extensions #
Les interfaces de définition d'extensions doivent hériter de l'interface Java "`java.io.Serializable`" et les implémentations de la classe abstraite "`java.rmi.server.UnicastRemoteObject`".