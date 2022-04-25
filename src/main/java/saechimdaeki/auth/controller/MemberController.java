package saechimdaeki.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/test")
    public EntityModel<?> helloTest(){
        String hello = "hello World!";
        ResponseEntity<String> helloResponse = ResponseEntity.ok(hello);
        EntityModel<?> helloWorld = EntityModel.of(helloResponse);
        helloWorld.add(linkTo(methodOn(MemberController.class).helloTest()).withSelfRel());

        return helloWorld;
    }
}
