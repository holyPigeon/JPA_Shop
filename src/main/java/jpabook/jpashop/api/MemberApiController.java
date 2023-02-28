package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

  private final MemberService memberService;

  /**
   * 회원가입 API Version 1
   */
  /*@PostMapping("/api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
    Long id = memberService.join(member);

    return new CreateMemberResponse(id);
  }*/

  /**
   * 회원가입 API Version 2
   */
  @PostMapping("/api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

    Member member = new Member();
    member.setName(request.getName());

    Long id = memberService.join(member);

    return new CreateMemberResponse(id);
  }

  /**
   * 회원 수정 API
   */
  @PutMapping("/api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {

    memberService.update(id, request.getName()); // 업데이트
    Member findMember = memberService.findOne(id); // 업데이트한 member를 찾음 -> 그것의 id와 name값을 사용
    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }

  /**
   * 회원 조회 API Version 1
   */
  /*@GetMapping("/api/v1/members")
  public List<Member> listMembersV1() {

    return memberService.findMembers();
  }*/

  /**
   * 회원 조회 API Version 2
   */
  @GetMapping("/api/v2/members")
  public Result listMembersV1() {
    List<Member> findMembers = memberService.findMembers();
    List<MemberDTO> collectMembers = findMembers.stream()
        .map(m -> new MemberDTO(m.getName()))
        .collect(Collectors.toList());

    return new Result(collectMembers.size(), collectMembers);
  }

  @Data
  static class CreateMemberRequest {

    private String name;
  }

  @Data
  @AllArgsConstructor
  static class CreateMemberResponse {

    private Long id;
  }

  @Data
  static class UpdateMemberRequest {

    private String name;
  }

  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {

    private Long id;
    private String name;
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {

    private int count;
    private T data;
  }

  @Data
  @AllArgsConstructor
  static class MemberDTO {

    private String name;
  }
}
