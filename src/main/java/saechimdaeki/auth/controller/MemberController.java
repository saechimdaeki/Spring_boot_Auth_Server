package saechimdaeki.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import saechimdaeki.auth.dto.JoinMemberDto;
import saechimdaeki.auth.service.MemberService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //TODO controllerAdvice 구현

    @GetMapping("/test")
    public EntityModel<?> helloTest(){
        String hello = "hello World!";
        ResponseEntity<String> helloResponse = ResponseEntity.ok(hello);
        EntityModel<?> helloWorld = EntityModel.of(helloResponse);
        helloWorld.add(linkTo(methodOn(MemberController.class).helloTest()).withSelfRel());

        return helloWorld;
    }

    @PostMapping("/sign-up")
    public EntityModel<?> signUp(@RequestBody JoinMemberDto dto){
        Long newMemberId = memberService.joinNewMember(dto);
        ResponseEntity<Long> signUpResponse = ResponseEntity.status(HttpStatus.CREATED).body(newMemberId);
        EntityModel<?> entityModel = EntityModel.of(signUpResponse);
        entityModel.add(linkTo(methodOn(MemberController.class).signUp(dto)).withSelfRel());
        return entityModel;
    }

    //TODO 반환 값 정의 필요
    @GetMapping("/check-email-token")
    public EntityModel<?> checkEmailToken(String token, String email){
        memberService.verifyToken(token,email);

        String result ="검증 완료";
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
        EntityModel<?> entityModel = EntityModel.of(response);
        entityModel.add(linkTo(methodOn(MemberController.class).checkEmailToken(token,email)).withSelfRel());
        return entityModel;
    }
}
