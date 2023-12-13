package com.blogapp.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogapp.config.AppConstants;
import com.blogapp.payloads.ApiResponse;
import com.blogapp.payloads.ImageResponse;
import com.blogapp.payloads.PostDto;
import com.blogapp.payloads.PostResponse;
import com.blogapp.services.FileService;
import com.blogapp.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;

//	Create
	@PostMapping("/user/{userId}/category/{categoryId}")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPostDto = this.postService.createPost(postDto, userId, categoryId);

		return new ResponseEntity<PostDto>(createdPostDto, HttpStatus.CREATED);

	}

//	Get by user
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
		List<PostDto> postDtos = this.postService.getPostsByUser(userId);
		return ResponseEntity.ok(postDtos);

	}

//	Get by category
	@GetMapping("/category/{categoryId}")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {
		List<PostDto> postDtos = this.postService.getPostsByCategory(categoryId);
		return ResponseEntity.ok(postDtos);

	}

//	Get all posts
	@GetMapping("")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		PostResponse postResponse = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

//	Get post by Id
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
	}

//	Delete post
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted successfully", true), HttpStatus.OK);
	}

//	Update post
	@PutMapping("/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatedPost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatedPost, HttpStatus.OK);
	}

//	Search
	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchByTitle(@PathVariable String keyword) {
		List<PostDto> searchedPosts = this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(searchedPosts, HttpStatus.OK);
	}

//	Post image upload
	@PostMapping("/imageUpload/{postId}")
	public ResponseEntity<ImageResponse> uploadPostImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId) {
		PostDto postDto = this.postService.getPostById(postId);
		String fileName = null;
		try {
			fileName = this.fileService.uploadImage(path, image);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<ImageResponse>(
					new ImageResponse(fileName, "Image is not uploaded due to server error"), HttpStatus.BAD_REQUEST);
			
		}
		System.out.println(fileName);
		
		postDto.setImageName(fileName);
		this.postService.updatePost(postDto, postId);
		return new ResponseEntity<ImageResponse>(new ImageResponse(fileName, "Image uploaded successfully"),
				HttpStatus.OK);

	}
	
//	Get image by postId
	@GetMapping(value="/getImage/{imageName}",produces= MediaType.IMAGE_JPEG_VALUE)
	public void getImagebyPostId(@PathVariable String imageName, HttpServletResponse response) {
		InputStream iStream = null;
		try {
			iStream = this.fileService.getResource(path, imageName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			FileCopyUtils.copy(iStream,response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
