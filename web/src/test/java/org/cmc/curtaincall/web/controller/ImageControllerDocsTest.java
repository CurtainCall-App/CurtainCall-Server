package org.cmc.curtaincall.web.controller;

import org.cmc.curtaincall.web.common.AbstractWebTest;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageController.class)
class ImageControllerDocsTest extends AbstractWebTest {

    @MockBean
    private ImageService imageService;

    @Test
    @WithMockUser
    void saveImage_Docs() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "test-image1.jpg",
                MediaType.IMAGE_JPEG_VALUE, "test-image1-content".getBytes());
        given(imageService.saveImage(any())).willReturn(new IdResult<>(10L));

        // expected
        mockMvc.perform(multipart("/images")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("image-save-image",
                        requestParts(
                                partWithName("image").description("이미지")
                        ),
                        responseFields(
                                fieldWithPath("id").description("저장된 이미지 ID")
                        )
                ))
        ;
    }
}