# Introduction #

Astenn permet la création d'[extensions réparties](ExtensionsReparties.md) à l'aide du protocole SOAP. En Java, la libraire [CXF](http://cxf.apache.org) est utilisée par Astenn pour mettre en oeuvre de ce support.

Une dépendance à "astenn-cxf" peut être ajoutée à votre projet grâce à [Maven](Maven.md).

# Format des adresses #
Les extensions exposées à l'aide d'astenn-cxf dispose d'une adresse de la forme : `soap:http://<mon_serveur>:<mon_port>/<classe_de_l_interface>/<classe_de_l_implementation>`. Par exemple : `soap:http://localhost:8080/monprojet.IMonExtension/monprojet.MonExtensionImpl`.

Si la servlet astenn-cxf est utilisée, l'adresse des extensions exposées sera plutôt de la forme : `soap:http://<mon_serveur>:<mon_port>/<nom_webapp>/<classe_de_l_interface>/<classe_de_l_implementation>`. Par exemple : `soap:http://localhost:8080/Astenn/monprojet.IMonExtension/monprojet.MonExtensionImpl`.

Si l'annotation [JAX-WS](http://jax-ws.java.net) "`@WebService`" est utilisée, l'attribut "name" de celle-ci est utilisé pour former une adresse secondaire plus courte de la forme : `soap:http://<mon_serveur>:<mon_port>/<attribut_name>`. Par exemple : `soap:http://localhost:8080/ServiceMonExtension`.

# Annotations #
Les annotations [JAX-WS](http://jax-ws.java.net) et [JAXB](http://jaxb.java.net), peuvent être utilisées pour définir précisément le format des données SOAP.
```
package monprojet;

@WebService(name="MyExtensionService", namespace="http://myproject")
public interface IMonExtension {

   @WebMethod(operationName="sayHello")
   @WebResult(name="result")
   MonBean ditCoucou(@WebParam(name="myBean") MonBean monBean);

}
```

```
package monprojet;

@XmlRootElement(name="mybean")
public interface MonBean {

   private String message;

   @XmlElement(name="message")
   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

}
```
# Servlet #
En production, l'utilisation de la servlet astenn-cxf est conseillée en lieu et place du serveur autonome. Pour utiliser celle-ci, ajouter au fichier "pom.xml" de votre application web les informations suivantes :

```

<?xml version="1.0" encoding="UTF-8"?>
<web-app>
    <servlet>
        <display-name>Servlet d'Astenn</display-name>
        <servlet-name>ServletAstenn</servlet-name>
        <servlet-class>org.lestr.astenn.cxf.AstennServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>ServletAstenn</servlet-name>
        <url-pattern>/astenn</url-pattern>
    </servlet-mapping>
</web-app>
```