package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        for (Order order : all){
            order.getMember().getName();
            order.getDelivery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> orderV2(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());

        return orderDTOList;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> orderV3(){
        List<Order> all = orderRepository.findAllWithItem();

        List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());

        return orderDTOList;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> orderV3_page(){
        List<Order> all = orderRepository.findAllWithMemberDelivery();

        List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o))
                .collect(Collectors.toList());

        return orderDTOList;
    }

    @Getter
    static class OrderDTO{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;

        public OrderDTO(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress();
            orderItems = order.getOrderItems().stream().map(orderItem ->
                    new OrderItemDTO(orderItem)).collect(Collectors.toList());
        }
    }

    @Getter
    static  class OrderItemDTO{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }


}
