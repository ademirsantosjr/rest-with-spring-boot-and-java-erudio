package br.com.erudio.erudioapi.repository;

import br.com.erudio.erudioapi.model.Person;
import br.com.erudio.erudioapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying // spring data nao manipula essa transacao. Garantir ACID.
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    void disablePerson(@Param("id") Long id);
}
