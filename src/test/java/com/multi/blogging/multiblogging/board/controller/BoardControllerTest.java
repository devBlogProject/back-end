package com.multi.blogging.multiblogging.board.controller;

import com.multi.blogging.multiblogging.board.dto.request.BoardImageUploadRequestDto;
import com.multi.blogging.multiblogging.imageUpload.service.ImageUploadService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @MockBean
    ImageUploadService imageUploadService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    void uploadImage() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("image", "image.jpg", "image/jpg", "<<jpg data>>".getBytes());
        given(imageUploadService.uploadFile(Mockito.any(MultipartFile.class))).willReturn("http://image.file");

        BoardImageUploadRequestDto requestDto = new BoardImageUploadRequestDto();
        requestDto.setImage(imageFile);

        mockMvc.perform(multipart("/board/image").file(imageFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value("http://image.file"));
    }
}