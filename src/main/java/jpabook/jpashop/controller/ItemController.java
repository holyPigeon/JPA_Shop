package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.UpdateItemDTO;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  /**
   * 상품 등록 페이지
   */
  @GetMapping("/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());

    return "items/createItemForm";
  }

  /**
   * 상품 등록
   */
  @PostMapping("/items/new")
  public String create(BookForm form) {

    Book book = new Book();

    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);

    return "redirect:/";
  }

  /**
   * 상품 목록 페이지
   */
  @GetMapping("/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);

    return "items/itemList";
  }

  /**
   * 상품 수정 페이지
   */
  @GetMapping("/items/{itemId}/edit")
  public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
    Book item = (Book) itemService.findOne(itemId);

    BookForm form = new BookForm();
    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setStockQuantity(item.getStockQuantity());
    form.setAuthor(item.getAuthor());
    form.setIsbn(item.getIsbn());

    model.addAttribute("form", form);

    return "items/updateItemForm";
  }

  @PostMapping("/items/{itemId}/edit")
  public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {

    // 병합(merge)를 사용한 수정 방법. 필요한 부분만 데이터가 바뀌는 게 아니라 필드 자체가 통째로 바뀌고
    // 특정 속성의 데이터 입력값이 없다면 그 속성값은 null이 되기 때문에 좋은 방법이 아님.
    /*
    Book book = new Book();
    book.setId(form.getId());
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    */

    // 변경 감지를 이용한 수정 방법 -> em.find(itemId)로 영속성 엔티티를 가져온 후 그것을 수정하면 변경 감지가 되는 듯?
    UpdateItemDTO itemDTO = new UpdateItemDTO();
    itemDTO.setName(form.getName());
    itemDTO.setPrice(form.getPrice());
    itemDTO.setStockQuantity(form.getStockQuantity());

    itemService.updateItem(itemId, itemDTO);

    return "redirect:/items";
  }
}
