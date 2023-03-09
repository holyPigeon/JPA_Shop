package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

  private final InitService initService;

  /**
   * 애플리케이션 실행 전에 미리 init() 실행
   */
  @PostConstruct
  public void init() {
    initService.dbInit1();
    initService.dbInit2();
  }

  @Component
  @Transactional
  @RequiredArgsConstructor
  static class InitService {

    private final EntityManager em;

    public void dbInit1() {

      Member member = createMember("userA", "서울", "1", "1111");
      em.persist(member);

      Book book1 = createBook("JPA1 BOOK", 10000);
      em.persist(book1);

      Book book2 = createBook("JPA2 BOOK", 20000);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

      Delivery delivery = createDelivery(member);

      Order order = Order.craeteOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    public void dbInit2() {

      Member member = createMember("userB", "진주", "2", "2222");
      em.persist(member);

      Book book1 = createBook("SPRING1 BOOK", 10000);
      em.persist(book1);

      Book book2 = createBook("SPRING2 BOOK", 20000);
      em.persist(book2);

      OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
      OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

      Delivery delivery = createDelivery(member);

      Order order = Order.craeteOrder(member, delivery, orderItem1, orderItem2);
      em.persist(order);

    }

    private Delivery createDelivery(Member member) {
      Delivery delivery = new Delivery();
      delivery.setAddress(member.getAddress());
      delivery.setStatus(DeliveryStatus.READY);
      return delivery;
    }

    private Book createBook(String JPA1_BOOK, int price) {
      Book book1 = new Book();
      book1.setName(JPA1_BOOK);
      book1.setPrice(price);
      book1.setStockQuantity(100);
      return book1;
    }

    private Member createMember(String userA, String 서울, String street, String zipcode) {
      Member member = new Member();
      member.setName(userA);
      member.setAddress(new Address(서울, street, zipcode));
      return member;
    }

  }
}
