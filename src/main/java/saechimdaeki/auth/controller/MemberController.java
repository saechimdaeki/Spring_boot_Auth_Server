package saechimdaeki.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saechimdaeki.auth.dto.JoinMemberDto;
import saechimdaeki.auth.dto.MemberInfoDto;
import saechimdaeki.auth.service.MemberService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //TODO controllerAdvice 구현

    @GetMapping("/test")
    public ResponseEntity<?> helloTest(){
        String hello = "hello World!";
        EntityModel<?> helloWorld = EntityModel.of(new ResponseDto<>(HttpStatus.OK.value(),hello));
        helloWorld.add(linkTo(methodOn(MemberController.class).helloTest()).withSelfRel());

        return ResponseEntity.ok(helloWorld);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody JoinMemberDto dto){
        Long newMemberId = memberService.joinNewMember(dto);
        EntityModel<?> entityModel = EntityModel.of(new ResponseDto<>(HttpStatus.CREATED.value(),newMemberId));
        entityModel.add(linkTo(methodOn(MemberController.class).signUp(dto)).withSelfRel());
        entityModel.add(linkTo(MemberController.class).slash(newMemberId).withRel("MemberDetailInfo"));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    //TODO 반환 값 정의 필요
    @GetMapping("/check-email-token")
    public ResponseEntity<?> checkEmailToken(String token, String email){
        memberService.verifyToken(token,email);

        String result ="검증 완료";
        EntityModel<?> entityModel = EntityModel.of(new ResponseDto<>(HttpStatus.ACCEPTED.value(),result));
        entityModel.add(linkTo(methodOn(MemberController.class).checkEmailToken(token,email)).withSelfRel());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(entityModel);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getMemberInfo(@PathVariable Long id){
        MemberInfoDto memberInfoDto = memberService.retriveMemberInfo(id);
        EntityModel<?> entityModel = EntityModel.of(new ResponseDto<>(HttpStatus.OK.value(),memberInfoDto));
        entityModel.add(linkTo(methodOn(MemberController.class).getMemberInfo(id)).withSelfRel());
        return ResponseEntity.ok(entityModel);
    }
}
