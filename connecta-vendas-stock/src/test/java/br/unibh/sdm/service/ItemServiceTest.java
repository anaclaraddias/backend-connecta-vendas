package br.unibh.sdm.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import br.unibh.sdm.entities.Item;
import br.unibh.sdm.repository.ItemRepository;

public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepo;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveItem_saves() {
        Item i = new Item("1", "Teclado", "Teclado mecânico", 10, 250.0);
        when(itemRepo.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Item saved = itemService.saveItem(i);
        assertNotNull(saved);
        assertEquals("Teclado", saved.getName());
        verify(itemRepo, times(1)).save(any(Item.class));
    }

    @Test
    void testGetAllItems() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("1", "Teclado", "Teclado mecânico", 10, 250.0));
        list.add(new Item("2", "Mouse", "Mouse óptico", 5, 80.0));
        when(itemRepo.findAll()).thenReturn(list);

        Iterable<Item> result = itemService.getAllItems();
        assertNotNull(result);
        List<Item> resList = new ArrayList<>();
        result.forEach(resList::add);
        assertEquals(2, resList.size());
        verify(itemRepo, times(1)).findAll();
    }

    @Test
    void testGetItemById_found() {
        Item i = new Item("1", "Teclado", "Teclado mecânico", 10, 250.0);
        when(itemRepo.findByCode("1")).thenReturn(i);
        Item res = itemService.getItemById("1");
        assertNotNull(res);
        assertEquals("1", res.getCode());
    }

    @Test
    void testGetItemById_notFound() {
        when(itemRepo.findByCode("missing")).thenReturn(null);
        Item res = itemService.getItemById("missing");
        assertNull(res);
    }

    @Test
    void testGetItemsByName_listReturned() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("1", "Teclado", "Teclado mecânico", 10, 250.0));
        when(itemRepo.findByName("Teclado")).thenReturn(list);
        Iterable<Item> res = itemService.getItemsByName("Teclado");
        assertNotNull(res);
        List<Item> resList = new ArrayList<>();
        res.forEach(resList::add);
        assertEquals(1, resList.size());
        assertEquals("Teclado", resList.get(0).getName());
    }

    @Test
    void testUpdateItem_success() {
        Item existing = new Item("1", "Teclado", "Antigo", 5, 200.0);
        when(itemRepo.findByCode("1")).thenReturn(existing);
        when(itemRepo.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Item incoming = new Item(null, "Teclado Gamer", "Atualizado", 15, 300.0);
        Item updated = itemService.updateItem("1", incoming);
        assertNotNull(updated);
        assertEquals("Teclado Gamer", updated.getName());
        assertEquals("Atualizado", updated.getDescription());
        assertEquals(15, updated.getQuantity());
        assertEquals(300.0, updated.getPrice());
        verify(itemRepo, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateItem_notFound() {
        when(itemRepo.findByCode("nope")).thenReturn(null);
        Item updated = itemService.updateItem("nope", new Item());
        assertNull(updated);
    }

    @Test
    void testDeleteItem_success() {
        Item existing = new Item("1", "Teclado", "Antigo", 5, 200.0);
        when(itemRepo.findByCode("1")).thenReturn(existing);
        doNothing().when(itemRepo).delete(existing);
        boolean deleted = itemService.deleteItem("1");
        assertTrue(deleted);
        verify(itemRepo, times(1)).delete(existing);
    }

    @Test
    void testDeleteItem_notFound() {
        when(itemRepo.findByCode("nope")).thenReturn(null);
        boolean deleted = itemService.deleteItem("nope");
        assertFalse(deleted);
    }
}
