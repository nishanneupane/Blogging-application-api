package com.blogging_app.service;

import java.util.List;

import com.blogging_app.payloads.CategoryDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);

	CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);

	void deleteCategory(Integer categoryId);
	
	List<CategoryDto> getAllCategory();
	
	CategoryDto getCategory(Integer categoryId);

}
