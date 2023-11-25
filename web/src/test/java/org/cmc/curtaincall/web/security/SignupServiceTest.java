package org.cmc.curtaincall.web.security;

import org.cmc.curtaincall.domain.account.Account;
import org.cmc.curtaincall.domain.account.repository.AccountRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.exception.MemberNicknameAlreadyExistsException;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.web.security.request.SignupRequest;
import org.cmc.curtaincall.web.security.service.SignupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void signup_success() {
        // given
        given(memberRepository.existsByNickname("test-nickname")).willReturn(false);
        final ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        final Member member = mock(Member.class);
        given(memberRepository.save(memberCaptor.capture())).willReturn(member);

        given(member.getId()).willReturn(10L);

        // when
        final SignupRequest request = SignupRequest.builder()
                .nickname("test-nickname")
                .build();
        signupService.signup("test-username", request);

        // then
        assertThat(memberCaptor.getValue().getNickname()).isEqualTo("test-nickname");
        final ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        then(accountRepository).should().save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getUsername()).isEqualTo("test-username");
        assertThat(accountCaptor.getValue().getMemberId()).isEqualTo(new MemberId(10L));
    }

    @Test
    void signup_given_existing_nickname_then_fail() {
        // given
        given(memberRepository.existsByNickname("test-nickname")).willReturn(true);

        // expected
        final SignupRequest request = SignupRequest.builder()
                .nickname("test-nickname")
                .build();
        assertThatThrownBy(() -> signupService.signup("test-username", request))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

}