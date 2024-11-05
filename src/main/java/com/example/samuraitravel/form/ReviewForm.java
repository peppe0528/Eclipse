package com.example.samuraitravel.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;

public class ReviewForm {
	
	@NotEmpty(message = "タイトルは必須です。")
	private String title;
	
	@NotEmpty(message = "内容は必須です。")
	private String content;
	private LocalDateTime createdAt;
	
	//GettersとSetters
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
