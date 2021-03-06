package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * V4
     */
    public List<OrderQueryDTO> findOrderQueryDTO(){
        List<OrderQueryDTO> result = findOrder();
        result.forEach(o -> {
            List<OrderItemQueryDTO> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /**
     * V4
     * orderId 별 컬레션 items 불러오기 - n + 1 문제가 발생
     */
    public List<OrderItemQueryDTO> findOrderItems(Long orderId){
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id,i.name,oi.orderPrice,oi.count) "+
                        "from OrderItem oi " +
                        "join Item i " +
                        "where oi.order.id = :orderId", OrderItemQueryDTO.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    /**
     * V5
     */
    public List<OrderQueryDTO> findAllByDTO_optimization() {
        List<OrderQueryDTO> result = findOrder();

        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    /**
     * V5
     * orderIds 에 대한 items 를 모두 불러온 뒤,
     * orderId 별로 Map 생성
     */
    private Map<Long, List<OrderItemQueryDTO>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDTO> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                        "from OrderItem oi " +
                        "join Item i " +
                        "where oi.order.id in :orderIds", OrderItemQueryDTO.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDTO -> orderItemQueryDTO.getOrderId()));
        return orderItemMap;
    }

    /**
     * V5
     * List<OrderQueryDTO> 에서 orderId 만 빼서 리스트 만들기
     */
    private List<Long> toOrderIds(List<OrderQueryDTO> result) {
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());
        return orderIds;
    }

    /**
     * V4, V5
     * 컬렉션 Items 제외한 데이터 불러오기
     */
    public List<OrderQueryDTO> findOrder() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDTO(o.id,m.name,o.orderDate, o.status, " +
                        "m.address)" +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderQueryDTO.class)
                .getResultList();
    }

    /**
     * V6
     * 중복된 데이터 모두 불러오기
     */
    public List<OrderFlatDTO> findAllByDTO_flat() {

        return em.createQuery(
                "select new " +
                        "jpabook.jpashop.repository.order.query.OrderFlatDTO(o.id,m.name,o.orderDate,o.status,d.address,i.name,oi.orderPrice,oi.count) " +
                        "from Order o " +
                        "join o.member m " +
                        "join o.delivery d " +
                        "join o.orderItems oi " +
                        "join oi.item i", OrderFlatDTO.class)
                .getResultList();
    }
}
