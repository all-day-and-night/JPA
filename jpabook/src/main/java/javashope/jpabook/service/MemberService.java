package javashope.jpabook.service;

import javashope.jpabook.domain.Member;
import javashope.jpabook.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    /**
     * @AllArgsConstuctor
     * @RequiredArgsContructor (final 변수만)
     * 등을 사용하면 아래 생성자를 따로 구현하지 않아도 스프링에서 자동으로 생성해줌
     */
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


    //회원 가입
    @Transactional // default : read only false
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //
    private void validateDuplicateMember(Member member) {
       List<Member> findMembers = memberRepository.findByName(member.getName());
       if(!findMembers.isEmpty()){
           throw new IllegalStateException("이미 존재하는 회원입니다.");
       }
    } // DB에 자체적으로 member을 unique하게 설정

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
