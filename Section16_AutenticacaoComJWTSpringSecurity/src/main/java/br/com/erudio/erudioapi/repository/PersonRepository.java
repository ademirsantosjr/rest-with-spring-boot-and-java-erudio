package br.com.erudio.erudioapi.repository;

import br.com.erudio.erudioapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
