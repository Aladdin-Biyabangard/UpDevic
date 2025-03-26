package com.aladdin.updevic001.controllers;

import com.team.updevic001.UpDevic001Application;
import com.team.updevic001.controllers.AdminController;
import com.team.updevic001.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
//@SpringBootTest(classes = UpDevic001Application.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testActivateUser() throws Exception {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        //Mocking methodun cagrilmasi
        doNothing().when(adminService).activateUser(uuid);
        //Performans testi
        mockMvc.perform(put("/api/admin/{uuid}/activate", uuid))
                .andExpect(status().isOk());
        verify(adminService).activateUser(uuid);


    }
}


