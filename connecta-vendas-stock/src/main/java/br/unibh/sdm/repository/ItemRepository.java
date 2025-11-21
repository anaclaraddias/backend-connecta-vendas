package br.unibh.sdm.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import br.unibh.sdm.entities.Item;

@EnableScan
public interface ItemRepository extends CrudRepository<Item, String> {
    Item findByCode(String code);
    Iterable<Item> findByName(String name);
}
