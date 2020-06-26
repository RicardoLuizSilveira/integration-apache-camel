### Camel and Spring

Dentro do arquivo de configuração do Spring devemos declarar o CamelContext:

```xml
<context:component-scan base-package="br.com.meupacote"/>

<camelContext xmlns="http://camel.apache.org/schema/spring">
    <contextScan/>
</camelContext>
```

Agora podemos injetar o CamelContext ou, usando a classe SpringRouteBuilder, definiremos a nova rota dentro de um Spring Bean:

```java
/**
 * Estendendo a classe SpringRouteBuilder não é preciso adicionar a rota programaticamente.
 */
@Component
public class MinhaRota extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        //aqui vem a rota!!
    }

}
```

Usando o Camel com Spring tem a grande vantagem que não é preciso de se preocupar com a abertura e fechamento do CamelContext. É o Spring quem cuida o CamelContext seguindo o princípio da inversão de controle. 