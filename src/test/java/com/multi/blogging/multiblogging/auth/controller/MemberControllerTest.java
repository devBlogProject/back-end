package com.multi.blogging.multiblogging.auth.controller;

import com.multi.blogging.multiblogging.auth.dto.request.MemberSignUpRequestDto;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.multi.blogging.multiblogging.Constant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageUploadService imageUploadService;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        MemberSignUpRequestDto signUpRequestDto = new MemberSignUpRequestDto();
        signUpRequestDto.setEmail(TEST_EMAIL);
        signUpRequestDto.setPassword(TEST_PASSWORD);
        signUpRequestDto.setNickName(TEST_NICK);
        mockMvc.perform(post("/member/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isCreated());
    }
    @Test
    @Transactional
    @WithMockUser(username =TEST_EMAIL)
    void updateProfileImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());
        given(imageUploadService.uploadFile(Mockito.any(MultipartFile.class))).willReturn("http://image.file");

        var builder = MockMvcRequestBuilders.multipart("/member/image");
        builder.with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }
        );


        mockMvc.perform(builder.file(imageFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.imageUrl").value("http://image.file"));
    }
}