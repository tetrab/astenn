# Introduction #
Le bus d'extensions d'Astenn permet à différentes applications en cours d'exécution de partager tout ou partie de leurs extensions. Le bus d'extensions s'appuie sur les [extensions réparties](ExtensionsReparties.md) d'Astenn et peut donc utiliser les protocoles SOAP, RMI ou .NET Remoting.

# Principe de la connexion d'une application à un bus Astenn #
Une application désirant partager tout ou partie de ses extensions avec un groupe d'applications locales ou distantes connectées à un bus Astenn doit simplement se contacter à l'une d'entre elles. L'ensemble des coordonnées des applications connectées au bus sont récupérées auprès de celle-ci.