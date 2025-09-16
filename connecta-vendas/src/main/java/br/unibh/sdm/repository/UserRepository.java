package br.unibh.sdm.repository;

import java.util.UUID;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import br.unibh.sdm.entities.User;

@EnableScan
public interface UserRepository extends CrudRepository<User, UUID> {
	User findById(String id);
}
