package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    /**
     * v1의 문제점은 검증 로직이 entity에 들어가 있기 때문에 처리하기 힘들다
     *
     * entity를 parameter로 받지 말고 DTO (Data Transfer Object)를 만들어 사용하자
     *
     * @param member
     * @return
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // class의 형태로 api 반환 -> json의 형태로 반환

    /**
     * 별도의 DTO를 구현하여 parameter를 받아 비즈니스 로직을 수정한다.
     * Entity를 외부로 드러내지 않고 사용할 수 있고 Entity를 따로 수정하지 않고 구현할 수 있다.
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    /**
     * 회원수정 api
     *
     * @param id
     * @param request
     * @return
     */
    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());


    }

    /**
     * 회원 전체 조회 api
     *
     * 문제는 memberService의 리턴값이 Entity list인데 전부 외부로 노출된다
     *
     * @return
     */
    @GetMapping("api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result memberV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * json 형식의 데이터의 "확장성"을 위해 사용해야 한다.
     *
     * wrapper class
     *
     * @param <T>
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    /**
     * 노출할 데이터만 특정하여 전달하도록 설계계     */
    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }



}
