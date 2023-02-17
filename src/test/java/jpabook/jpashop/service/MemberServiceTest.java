package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;

  @Test
//  @Rollback(false)
  public void 회원가입() throws Exception {
    //given
    Member member = new Member();
    member.setName("memberA");

    //when
    Long savedId = memberService.join(member);
    Member findMember = memberService.findOne(savedId);

    //then
    Assertions.assertThat(findMember).isEqualTo(member);

  }

  @Test(expected = IllegalStateException.class) // 해당 예외 발생 시 테스트 성공
  public void validateDuplicateMemberTest() throws Exception {
    //given
    Member memberA = new Member();
    memberA.setName("memberA");

    Member memberB = new Member();
    memberB.setName("memberA");

    //when
    memberService.join(memberA);
    memberService.join(memberB);

    //then
    fail("예외가 발생하면 안됨");

  }

}