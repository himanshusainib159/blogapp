package com.blogapp.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter	
public class CategoryDto {

	private int categoryId;

	@NotBlank
	@Size(min = 4, message = "minimum 4 characters")
	private String categoryTitle;

	@NotBlank
	@Size(max = 10,message = "maximum size allowed is 10")
	private String categoryDescription;
}
