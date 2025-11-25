package br.unibh.sdm.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unibh.sdm.entities.Item;
import br.unibh.sdm.service.ItemService;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createItem_returnsCreated() throws Exception {
        Item request = new Item("Teclado","Mecânico",10,250.0);
        Item response = new Item("Teclado","Mecânico",10,250.0);
        
        when(itemService.saveItem(any(Item.class))).thenReturn(response);
        
        mvc.perform(post("/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Teclado"));
    }

    @Test
    void listItems_returnsArray() throws Exception {
        Item first = new Item("Teclado","Mecânico",10,250.0);
        Item second = new Item("Mouse","Óptico",5,80.0);
        when(itemService.getAllItems()).thenReturn(Arrays.asList(first, second));
        mvc.perform(get("/item").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getItemById_found() throws Exception {
        Item item = new Item("Teclado","Mecânico",10,250.0);
        when(itemService.getItemById("some-uuid")).thenReturn(item);
        mvc.perform(get("/item/{id}", "some-uuid").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Teclado"));
    }

    @Test
    void getItemById_notFound() throws Exception {
        when(itemService.getItemById("2")).thenReturn(null);
        mvc.perform(get("/item/{id}", "2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getItemsByName_returnsArray() throws Exception {
        Item item = new Item("Teclado","Mecânico",10,250.0);
        when(itemService.getItemsByName("Teclado")).thenReturn(Arrays.asList(item));
        mvc.perform(get("/item/name/{name}", "Teclado").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Teclado"));
    }

    @Test
    void updateItem_success() throws Exception {
        Item request = new Item("Teclado Gamer","Atualizado",15,300.0);
        Item response = new Item("Teclado Gamer","Atualizado",15,300.0);
        when(itemService.updateItem(eq("some-uuid"), any(Item.class))).thenReturn(response);
        mvc.perform(put("/item/{id}", "some-uuid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Teclado Gamer"));
    }

    @Test
    void updateItem_notFound() throws Exception {
        when(itemService.updateItem(eq("missing"), any(Item.class))).thenReturn(null);
        mvc.perform(put("/item/{id}", "missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteItem_success() throws Exception {
        when(itemService.deleteItem("1")).thenReturn(true);
        mvc.perform(delete("/item/{id}", "1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteItem_notFound() throws Exception {
        when(itemService.deleteItem("missing")).thenReturn(false);
        mvc.perform(delete("/item/{id}", "missing"))
            .andExpect(status().isNotFound());
    }
}
