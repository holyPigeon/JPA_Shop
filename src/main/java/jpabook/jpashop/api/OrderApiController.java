package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());


    // 지연로딩 설정된 변수들을 초기화함으로써 값을 불러옴 -> all 리턴값에 Member, Delivery, OrderItems 등을 표시
    // hibernate5 module 사용 시 초기화해야 함
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();
      order.getOrderItems().stream().forEach(o -> o.getItem().getName());
    }

    return all;
  }
  
  @GetMapping("/api/v2/orders")
  public List<OrderDTO> ordersV2() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());

    List<OrderDTO> result = all.stream()
        .map(o -> new OrderDTO(o))
        .collect(Collectors.toList());

    return result;
  }

  @GetMapping("/api/v3/orders")
  public List<OrderDTO> ordersV3() {
    List<Order> all = orderRepository.findAllWithMemberDeliveryItem();

    List<OrderDTO> result = all.stream()
        .map(o -> new OrderDTO(o))
        .collect(Collectors.toList());

    return result;
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDTO> ordersV3_page(
      @RequestParam(value="offset", defaultValue = "0") int offset,
      @RequestParam(value="limit", defaultValue = "100") int limit
  ) {
    List<Order> all = orderRepository.findAllWithMemberDelivery2(offset, limit);

    List<OrderDTO> result = all.stream()
        .map(o -> new OrderDTO(o))
        .collect(Collectors.toList());

    return result;
  }
  
  @Data
  static class OrderDTO {
    
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getStatus();
      address = order.getDelivery().getAddress();
      orderItems = order.getOrderItems().stream()
          .map(o -> new OrderItemDTO(o))
          .collect(Collectors.toList());
    }
  }

  @Data
  static class OrderItemDTO {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDTO(OrderItem orderItem) {
      itemName = orderItem.getItem().getName();
      orderPrice = orderItem.getOrderPrice();
      count = orderItem.getCount();
    }
  }
}
