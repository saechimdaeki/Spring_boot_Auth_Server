package saechimdaeki.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import saechimdaeki.auth.config.CommonConfig;
import saechimdaeki.auth.dto.JoinMemberDto;
import saechimdaeki.auth.service.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ExtendWith(SpringExtension.class)
@Import(CommonConfig.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @Test
    @DisplayName("테스트용도로 남겨놓는 endPoint 테스트")
    void restTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/test"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                                .andExpect(jsonPath("body").exists())
                                .andDo(print())
                                .andReturn();

        Object body = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.body");
        String bodyString = objectMapper.writeValueAsString(body);

        assertThat(bodyString).contains("hello World!");
    }

    @Test
    @DisplayName("회원가입")
    void 회원가입_endPoint() throws Exception {
        JoinMemberDto joinMemberDto =
            new JoinMemberDto("saechimdaeki","anima94@kakao.com","1234");
        String joinMemberDtoJson = objectMapper.writeValueAsString(joinMemberDto);

        given(memberService.joinNewMember(any())).willReturn(1L);

        MockHttpServletResponse response = mockMvc.perform(post("/sign-up")
                                                              .contentType(MediaType.APPLICATION_JSON)
                                                              .content(joinMemberDtoJson))
                                                 .andExpect(status().isCreated())
                                                  .andExpect(content().contentType(MediaTypes.HAL_JSON))
                                                 .andExpect(jsonPath("$.statusCode", is(equalTo(201))))
                                                 .andExpect(jsonPath("$.body", is(equalTo(1))))
                                                 .andDo(print()).andReturn().getResponse();

        assertThat(response).isNotNull();
    }

}