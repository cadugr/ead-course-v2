package com.ead.course.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.ead.course.models.CourseModel;

public interface CourseRepository extends CrudRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel>{

}
