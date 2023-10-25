package com.bitbox.notification.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class NotificationControllerTests {

    @Autowired
    NotificationController notificationController;

    @Autowired
    MockMvc mockMvc;

    final String memberId = "avacasd-agretgeh-tntdvd";

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    @DisplayName("SSE 연결 테스트")
    void subscibeTest() throws Exception {
        String memberId = "ascacasc-assdasd-asdasd";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/notifications/subscription")
                .header("memberId", memberId);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }
}
