package com.ead.course.controllers;

import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
	
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	UserService courseUserService;
	
	@GetMapping("/courses/{courseId}/users")
	public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC )
													  		  Pageable pageable,
													  		  @PathVariable(value = "courseId") UUID courseId) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
		if(!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	@PostMapping("/courses/{courseId}/users/subscription")
	public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
																@RequestBody @Valid SubscriptionDto subscriptionDto) {
		Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
		if(!courseModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
		}
		//TODO: verificações state transfer
		return ResponseEntity.status(HttpStatus.CREATED).body("");
	}


}
