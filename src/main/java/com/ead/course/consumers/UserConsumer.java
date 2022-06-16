package com.ead.course.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ead.course.dtos.UserEventDto;
import com.ead.course.enums.ActionType;
import com.ead.course.services.UserService;


@Component
public class UserConsumer {
	
	@Autowired
	UserService userService;
	
	/**
	 Quando criamos um consumer e anotamos ele com @RabbitListener informando a fila 
	 e o exchange,ao iniciar a aplicação estaremos iniciando uma conexão com o rabbitmq e
	 a fila será criada, caso não exista, assim como o exchange.
	 */
	//incluído apenas por não termos definido a cláusula default do switch case
	@SuppressWarnings("incomplete-switch")
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "${ead.broker.queue.userEventQueue.name}", durable = "true"),
			exchange = @Exchange(value = "${ead.broker.exchange.userEventExchange}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")))
	public void listenUserEvent(@Payload UserEventDto userEventDto) {
		//aqui, precisamos definir o que vamos fazer quando recebermos esse payload, esta carga útil
		var userModel = userEventDto.convertToUserUserModel();
		
		switch (ActionType.valueOf(userEventDto.getActionType())) {
		case CREATE:
		case UPDATE:
			userService.save(userModel);
			break;
		case DELETE:
			userService.delete(userEventDto.getUserId());
			break;

		}
	}	

}
