package br.unibh.sdm.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.unibh.sdm.entities.Item;

@SpringBootTest
@ActiveProfiles("test")
public class ItemServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemServiceTest.class);

    @Autowired
    private ItemService itemService;

    @Test
    void testeCriacaoEListagem() {
        LOGGER.info("Criando itens via service...");
        Item i1 = itemService.saveItem(new Item("Teclado", "Teclado mecânico", 10, 250.0));
        Item i2 = itemService.saveItem(new Item("Mouse", "Mouse óptico", 5, 80.0));
        Item i3 = itemService.saveItem(new Item("Monitor", "Monitor 24'", 3, 900.0));

        assertNotNull(i1.getCode());
        assertNotNull(i2.getCode());
        assertNotNull(i3.getCode());

        LOGGER.info("Listando todos os itens...");
        Iterable<Item> lista = itemService.getAllItems();
        assertNotNull(lista.iterator());
        List<Item> resList = new ArrayList<>();
        lista.forEach(resList::add);
        assertTrue(resList.size() >= 3);

        LOGGER.info("Listando itens por nome...");
        Iterable<Item> monitores = itemService.getItemsByName("Monitor");
        List<Item> monitoresList = new ArrayList<>();
        monitores.forEach(monitoresList::add);
        assertFalse(monitoresList.isEmpty());
        assertEquals("Monitor 24'", monitoresList.get(0).getDescription());
    }

    @Test
    void testeBuscaPorIdEAtualizacao() {
        Item salvo = itemService.saveItem(new Item("Teclado", "Antigo", 5, 200.0));
        String code = salvo.getCode();
        assertNotNull(code);

        LOGGER.info("Buscando item por código...");
        Item encontrado = itemService.getItemById(code);
        assertNotNull(encontrado);
        assertEquals("Teclado", encontrado.getName());

        LOGGER.info("Atualizando item...");
        Item atualizacao = new Item("Teclado Gamer", "Atualizado", 15, 300.0);
        Item atualizado = itemService.updateItem(code, atualizacao);
        assertNotNull(atualizado);
        assertEquals("Teclado Gamer", atualizado.getName());
        assertEquals("Atualizado", atualizado.getDescription());
        assertEquals(15, atualizado.getQuantity());
        assertEquals(300.0, atualizado.getPrice());
    }

    @Test
    void testeDelete() {
        Item salvo = itemService.saveItem(new Item("Mouse", "Para deletar", 2, 50.0));
        String code = salvo.getCode();
        assertNotNull(code);

        LOGGER.info("Deletando item...");
        boolean deletado = itemService.deleteItem(code);
        assertTrue(deletado);

        Item depois = itemService.getItemById(code);
        assertNull(depois);
    }
}
