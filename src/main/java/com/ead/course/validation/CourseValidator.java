package com.ead.course.validation;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;

@Component
public class CourseValidator implements Validator {
	
	@Autowired
	@Qualifier("defaultValidator")
	private Validator validator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	AuthenticationCurrentUserService authenticationCurrentUserService;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object o, Errors errors) {
		//primeiro fazemos um cast para o tipo que desejamos
		CourseDto courseDto = (CourseDto) o;
		//Faz as validações que já estavam sendo feitas com o @Valid...mesmo comportamento
		validator.validate(courseDto, errors);
		//Se passou pelas vaidações, faz uma nova, customizada
		if(!errors.hasErrors()) {
			validateUserInstructor(courseDto.getUserInstructor(), errors);
		}
	}
	
	private void validateUserInstructor(UUID userInstructor, Errors errors) {
		UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
		
		if(currentUserId.equals(userInstructor)) {
			Optional<UserModel> userModelOptional = userService.findById(userInstructor);
			if(!userModelOptional.isPresent()) {
				errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
			}
			if(userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
				errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
			}			
		} else {
			throw new AccessDeniedException("Forbidden");
		}
	}

	
	
}
