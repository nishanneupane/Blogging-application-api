package com.blogging_app.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private int id;
	
	@NotBlank
	@Size(min=10)
	private String categoryTitle;
	
	@NotBlank
	@Size(min=10)
	private String categoryDescription;
}
