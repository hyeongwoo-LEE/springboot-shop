package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;


   public List<OrderDTO> ordersV3(){

       List<Order> all = orderRepository.findAllWithMemberDelivery();

       List<OrderDTO> orderDTOList = all.stream().map(o -> new OrderDTO(o))
               .collect(toList());

       return orderDTOList;

   }

}
