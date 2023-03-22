package com.psj.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map){
        map.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map){
        map.addAttribute("article", "article"); // TODO: 구현할 때 여기에 실제 데이터를 넣어야 한다.
        map.addAttribute("articleComments",List.of());
        return "articles/detail";

//  1. @GetMapping path의 {변수} 괄호안에 괄호 그레이스를 열어 URI에 사용될 변수 명을 입력한다.
//
//  GetMapping 뿐만 아니라 당연히 다른 Method도 가능하다.  ( PostMapping, DeleteMapping, PutMapping......)
//
//  2. @PathVariable 어노테이션을 이용해서 {템플릿 변수} 와 동일한 이름을 갖는 파라미터를 추가하면 된다.
    }

}
