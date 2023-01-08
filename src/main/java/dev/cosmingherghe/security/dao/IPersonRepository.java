package dev.cosmingherghe.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cosmingherghe.security.entity.Person;

public interface IPersonRepository extends JpaRepository<Person, Long> {

}
