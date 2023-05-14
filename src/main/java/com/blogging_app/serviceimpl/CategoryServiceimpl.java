package com.blogging_app.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogging_app.entity.Category;
import com.blogging_app.exception.ResourceNotFoundException;
import com.blogging_app.payloads.CategoryDto;
import com.blogging_app.repository.CategoryRepo;
import com.blogging_app.service.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService{
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category cat = modelMapper.map(categoryDto, Category.class);
		
		Category savedcat = categoryRepo.save(cat);
		
		return modelMapper.map(savedcat, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
		Category cat = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ", " Category Id", categoryId));
		
			cat.setCategoryTitle(categoryDto.getCategoryTitle());
			cat.setCategoryDescription(categoryDto.getCategoryDescription());
			
			Category save = categoryRepo.save(cat);
		
		return modelMapper.map(save, CategoryDto.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category cat = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		categoryRepo.delete(cat);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		
		List<Category> cats = categoryRepo.findAll();
		List<CategoryDto> catDto = cats.stream().map(cat->modelMapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		
		
		return catDto;
	}

	@Override
	public CategoryDto getCategory(Integer categoryId) {
		Category cat = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "Category Id", categoryId));
		
		
		return modelMapper.map(cat, CategoryDto.class);
	}

}
