package org.cmc.curtaincall.web.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs
public abstract class AbstractWebTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AccountDao accountDao;

    @BeforeEach
    void setUpAccountDao() {
        given(accountDao.findMemberIdByUsername(any()))
                .willReturn(Optional.of(new MemberId(1234L)));
    }
}
