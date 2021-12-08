package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDTO;
import jpabook.jpashop.repository.order.query.OrderItemQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryDTO;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderDTO;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;


@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderQueryService orderQueryService;

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

        List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o)).collect(toList());

        return orderDTOList;
    }

    @GetMapping("/api/v3/orders")
    public List<jpabook.jpashop.service.query.OrderDTO> orderV3(){

        return orderQueryService.ordersV3();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> orderV3_page(){
        List<Order> all = orderRepository.findAllWithMemberDelivery();

        List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o))
                .collect(toList());

        return orderDTOList;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDTO> orderV4(){
        return orderQueryRepository.findOrderQueryDTO();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDTO> orderV5(){
        return orderQueryRepository.findAllByDTO_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDTO> orderV6(){
        List<OrderFlatDTO> flats = orderQueryRepository.findAllByDTO_flat();

        //중복 제거
        return flats.stream()
               .collect(groupingBy(o -> new OrderQueryDTO(o.getOrderId(),
                               o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                       mapping(o -> new OrderItemQueryDTO(o.getOrderId(),
                               o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
               )).entrySet().stream()
                .map(e -> new OrderQueryDTO(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
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
                    new OrderItemDTO(orderItem)).collect(toList());
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
