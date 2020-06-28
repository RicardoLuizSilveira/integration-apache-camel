package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				errorHandler(
					    deadLetterChannel("file:erro").
					        logExhaustedMessageHistory(true).
					        maximumRedeliveries(3).
					            redeliveryDelay(5000).
					        onRedelivery(new Processor() {            
					            @Override
					            public void process(Exchange exchange) throws Exception {
					                int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
					                int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
					                System.out.println("Redelivery - " + counter + "/" + max );
					            }
					        })
					);
				
				from("file:pedidos?delay=5s&noop=true").
				routeId("rota-pedido").
					to("validator:pedido.xsd");//nova validação
//					multicast().
//						to("direct:soap").
//						to("direct:http");
				
				from("direct:http").
					routeId("rota-http").
					setProperty("pedidoId", xpath("/pedido/id/text()")).
					setProperty("clienteId", xpath("/pedido/pagamento/email-titular/text()")).
					split().
						xpath("/pedido/itens/item").
					filter().
						xpath("/item/formato[text()='EBOOK']").
					setProperty("ebookId", xpath("/item/livro/codigo/text()")).
					marshal()
						.xmljson().
					setHeader(Exchange.FILE_NAME, simple("${file:name.noext}-${header.CamelSplitIndex}.json")).
				log("${id} - ${body}").
				setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET)).
				setHeader(Exchange.HTTP_QUERY, simple("ebookId=${property.ebookId}&pedidoId=${property.pedidoId}&clienteId=${property.clienteId}")).
				to("http4://localhost:8080/webservices/ebook/item");
				
				from("direct:soap").
					routeId("rota-soap").
					setBody(constant("<envelopeId>45875</envelopeId>")).
				to("mock:soap");
				
			}
		});
		
		context.start();
		Thread.sleep(5000);
		context.stop();

	}	
}
