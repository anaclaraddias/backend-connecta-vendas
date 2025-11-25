package br.unibh.sdm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unibh.sdm.entities.Item;

public interface ItemRdsRepository extends JpaRepository<Item, Long> {
	Item findByCode(String code);
	List<Item> findByName(String name);
}
