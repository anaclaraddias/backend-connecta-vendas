package br.unibh.sdm.service;

import org.springframework.stereotype.Service;

import br.unibh.sdm.entities.Item;
import br.unibh.sdm.repository.ItemRepository;

@Service
public class ItemService {

    private final ItemRepository itemRepo;

    public ItemService(ItemRepository itemRepository){
        this.itemRepo = itemRepository;
    }

    public Item saveItem(Item item){
        return this.itemRepo.save(item);
    }

    public Iterable<Item> getAllItems(){
        return this.itemRepo.findAll();
    }

    public Item getItemById(String code){
        return this.itemRepo.findByCode(code);
    }

    public Iterable<Item> getItemsByName(String name){
        return this.itemRepo.findByName(name);
    }

    public Item updateItem(String code, Item item){
        Item existing = getItemById(code);
        if(existing == null){
            return null;
        }
        existing.setName(item.getName());
        existing.setDescription(item.getDescription());
        existing.setQuantity(item.getQuantity());
        existing.setPrice(item.getPrice());
        return this.itemRepo.save(existing);
    }

    public boolean deleteItem(String code){
        Item existing = getItemById(code);
        if(existing == null){
            return false;
        }
        this.itemRepo.delete(existing);
        return true;
    }
}
