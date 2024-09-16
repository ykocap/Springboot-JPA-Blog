package com.kbs.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kbs.blog.model.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	

}
