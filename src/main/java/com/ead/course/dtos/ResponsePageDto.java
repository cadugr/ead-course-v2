package com.ead.course.dtos;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ResponsePageDto<T> extends PageImpl<T>{

	private static final long serialVersionUID = 1L;

	//Colocamos essa anotação para mostrar para o jackson que queremos serializar esse dto na resposta.
	//Como temos vários parâmetros utilizamos o PROPERTIES, caso contrário seria o DELEGATING
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public ResponsePageDto(@JsonProperty("content") List<T> content,
						   @JsonProperty("number") int number,
						   @JsonProperty("size") int size,
						   @JsonProperty("totalElements") Long totalElements,
						   @JsonProperty("pageable") JsonNode pageable,
						   @JsonProperty("last") boolean last,
						   @JsonProperty("totalPages") int totalPages,
						   @JsonProperty("sort") JsonNode sort,
						   @JsonProperty("first") boolean first,
						   @JsonProperty("empty") boolean empty) {
		//Utilizamos o JsonProperty para especificar o nome das propriedades
		super(content, PageRequest.of(number, size), totalElements);
	}
	
	public ResponsePageDto(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}
	
	public ResponsePageDto(List<T> content) {
		super(content);
	}

}
