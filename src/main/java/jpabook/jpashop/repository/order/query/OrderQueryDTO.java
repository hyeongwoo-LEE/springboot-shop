package jpabook.jpashop.repository.order.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = "orderId")
@Data
public class OrderQueryDTO {

    private Long orderId;
    private String name;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDTO> orderItems;

    public OrderQueryDTO(Long orderId, String name, LocalDateTime localDateTime,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.localDateTime = localDateTime;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public OrderQueryDTO(Long orderId, String name, LocalDateTime localDateTime,
                         OrderStatus orderStatus, Address address, List<OrderItemQueryDTO> orderItems) {
        this.orderId = orderId;
        this.name = name;
        this.localDateTime = localDateTime;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems;
    }
}
