package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDTO> findOrderDTOList(){
        return em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDTO(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderSimpleQueryDTO.class)
                .getResultList();
    }
}
