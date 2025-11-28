package com.zhonghua.zhonghuaspetitions.controller;

import com.zhonghua.zhonghuaspetitions.model.Petition;
import com.zhonghua.zhonghuaspetitions.service.PetitionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetitionController.class)
class PetitionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetitionService petitionService;

    @Test
    void home_redirectsToPetitions() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/petitions"));
    }

    @Test
    void list_rendersListViewWithModel() throws Exception {
        when(petitionService.findAll()).thenReturn(List.of(
                new Petition(1L, "Title A", "Desc A", "Alice"),
                new Petition(2L, "Title B", "Desc B", "Bob")));

        mvc.perform(get("/petitions"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attributeExists("petitions"))
                .andExpect(model().attribute("petitions", hasSize(2)));
    }

    @Test
    void createForm_rendersCreateView() throws Exception {
        mvc.perform(get("/petitions/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"));
    }

    @Test
    void createSubmit_callsServiceAndRedirects() throws Exception {
        mvc.perform(post("/petitions/create")
                .param("title", "New Petition")
                .param("description", "Desc")
                .param("author", "Nancy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/petitions"));

        verify(petitionService).create("New Petition", "Desc", "Nancy");
    }

    @Test
    void searchForm_rendersSearchView() throws Exception {
        mvc.perform(get("/petitions/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void searchResults_callsServiceAndRendersResults() throws Exception {
        when(petitionService.searchByTitle("climate"))
                .thenReturn(List.of(new Petition(3L, "Climate Act", "Desc", "Carol")));

        mvc.perform(get("/petitions/search/results").param("q", "climate"))
                .andExpect(status().isOk())
                .andExpect(view().name("searchResults"))
                .andExpect(model().attributeExists("results", "q"))
                .andExpect(model().attribute("results", hasSize(1)))
                .andExpect(model().attribute("q", "climate"));
    }

    @Test
    void detail_rendersDetailViewWithPetition() throws Exception {
        Petition p = new Petition(5L, "Education Reform", "Desc", "Eve");
        when(petitionService.findById(5L)).thenReturn(Optional.of(p));

        mvc.perform(get("/petitions/5"))
                .andExpect(status().isOk())
                .andExpect(view().name("detail"))
                .andExpect(model().attributeExists("petition"))
                .andExpect(model().attribute("petition", p));
    }

    @Test
    void sign_callsServiceAndRedirectsToDetail() throws Exception {
        mvc.perform(post("/petitions/7/sign")
                .param("name", "John Doe")
                .param("email", "john@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/petitions/7"));

        verify(petitionService).sign(7L, "John Doe", "john@example.com");
    }

    // Helpers for Hamcrest size matcher without adding explicit dependency in test
    private org.hamcrest.Matcher<java.util.Collection<?>> hasSize(int size) {
        return new org.hamcrest.TypeSafeMatcher<>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("a collection with size ").appendValue(size);
            }

            @Override
            protected boolean matchesSafely(java.util.Collection<?> item) {
                return item != null && item.size() == size;
            }
        };
    }
}