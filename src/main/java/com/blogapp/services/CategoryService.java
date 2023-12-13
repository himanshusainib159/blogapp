package com.blogapp.services;

import java.util.List;

import com.blogapp.payloads.CategoryDto;


public interface CategoryService {

//	Create
	CategoryDto createCategory(CategoryDto categoryDto);
	
//	Update
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	
//	Delete
	void deleteCategory(Integer categoryId);
	
//	Get
	CategoryDto getCategory(Integer categoryId);

	
//	Get all
	List<CategoryDto> getCategories();
}
