package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {

}
