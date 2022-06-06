package com.ead.course.models;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_USERS")
public class UserModel implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	//Não precisamos gerar o id, pois ele será passado pelo microservice de authuser por meio de state transfer
	private UUID id;
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	@Column(nullable = false, length = 150)
	private String fullName;
	@Column(nullable = false)
	//Estamos utilizando como String ao invés de enum para não manter um vínculo forte com authuser, pois se mudar o enum lá, tem que mudar aqui
	//e também pois não precisamos seguir fielmente a tabela de usuários de authuser
	private String userStatus;
	@Column(nullable = false)
	private String userType;
	@Column(length = 20)
	private String cpf;
	@Column
	private String imageUrl;
	
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<CourseModel> courses;

}
