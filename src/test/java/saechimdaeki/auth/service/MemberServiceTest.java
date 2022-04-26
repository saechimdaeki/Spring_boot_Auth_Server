package saechimdaeki.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import saechimdaeki.auth.domain.Member;
import saechimdaeki.auth.dto.EmailMessage;
import saechimdaeki.auth.dto.JoinMemberDto;
import saechimdaeki.auth.dto.MemberDto;
import saechimdaeki.auth.repository.MemberRepository;
import saechimdaeki.auth.smtp.EmailService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Slf4j
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    EmailService emailService;


    @Test
    @DisplayName("회원 가입 시나리오")
    void 회원가입(){
        Member saveMember = memberRepository.save(Member.joinMember(
            new JoinMemberDto("saechimdaeki", "anima94@kakao.com", "1234")));

        MemberDto savedMemberDto = Member.toDto(saveMember);

        /* test코드에서 email을 보내는 행위는 멈춰..! */
        BDDMockito.doNothing().when(emailService).sendEmail(EmailMessage.sendSignUpEmail(savedMemberDto));

        Member findMember = memberRepository.findByUserName("saechimdaeki").orElseThrow(() -> new UsernameNotFoundException("없는 유저 이름입니다"));

        assertThat(findMember).isNotNull();
        assertThat(saveMember.getEmail()).isEqualTo(findMember.getEmail());
        assertThat(saveMember.getPassword()).isEqualTo(findMember.getPassword()); //패스워드 암호화는 MemberService 로직 이므로 비교는 이게맞다고 판단.
        assertThat(saveMember.getUserName()).isEqualTo(findMember.getUserName());

    }

}