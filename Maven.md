# Utilisation de Maven #

Astenn peut être trouvé dans le dépot [Maven](http://maven.apache.org) de [Nexus](http://oss.sonatype.org). Pour l'ajouter à votre projet, vous devez tout d'abord ajouter au fichier _"pom.xml"_ de votre projet ou à votre fichier _"<répertoire\_utilisateur>/.m2/settings.xml"_ le code suivant :

```
<repositories>
    
    <repository>
        <id>OSS-Sonatype SNAPSHOTS</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>

</repositories>
```

## Ajout du noyau Astenn ##

Ajouter le code suivant au fichier _"pom.xml"_ de votre projet :

```

<dependencies>

    <dependency>
        <groupId>org.lestr.astenn</groupId>
        <artifactId>astenn-core</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```

## Ajout du support CXF ##

Si vous souhaitez utiliser des [extensions réparties](ExtensionsReparties.md) via [SOAP](SOAP.md), ajouter le code suivant au fichier _"pom.xml"_ de votre projet :

```

<dependencies>

    <dependency>
        <groupId>org.lestr.astenn</groupId>
        <artifactId>astenn-cxf</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```

## Ajout du support RMI ##

Si vous souhaitez utiliser des [extensions réparties](ExtensionsReparties.md) via [RMI](RMI.md), ajouter le code suivant au fichier _"pom.xml"_ de votre projet :

```

<dependencies>

    <dependency>
        <groupId>org.lestr.astenn</groupId>
        <artifactId>astenn-rmi</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```

## Ajout du bus d'extensions Astenn ##

Si vous souhaitez utiliser le [bus d'extensions](Bus.md) Astenn, ajouter le code suivant au fichier _"pom.xml"_ de votre projet :

```

<dependencies>

    <dependency>
        <groupId>org.lestr.astenn</groupId>
        <artifactId>astenn-bus</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```

## Ajout de l'intégration à Liferay ##

Si vous souhaitez utiliser l'[intégration à Liferay](Liferay.md) d'Astenn, ajouter le code suivant au fichier _"pom.xml"_ de votre projet :

```

<dependencies>

    <dependency>
        <groupId>org.lestr.astenn</groupId>
        <artifactId>astenn-liferay</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
</dependencies>
```