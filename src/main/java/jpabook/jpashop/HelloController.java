package jpabook.jpashop;

import jpabook.jpashop.Hello;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

  @GetMapping("hello")
  public String hello(Model model) {
    model.addAttribute("data", "hello2!");

    Hello hello = new Hello();
    Hello hello2 = new Hello();


    return "hello";
  }
}
