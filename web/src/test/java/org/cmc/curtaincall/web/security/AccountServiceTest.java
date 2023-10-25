package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.web.security.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountDao accountDao;



    @Test
    void getMemberId() {
    }
}