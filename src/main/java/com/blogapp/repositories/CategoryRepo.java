package com.blogapp.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.blogapp.entities.Category;

public interface CategoryRepo extends JpaRepositoryImplementation<Category, Integer>{
	
}
