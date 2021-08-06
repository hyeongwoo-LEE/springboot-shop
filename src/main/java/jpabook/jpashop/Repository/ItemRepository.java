package jpabook.jpashop.Repository;

import jpabook.jpashop.domain.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.persistence.EntityManager;

@Repository
public class ItemRepository {

    private final EntityManager em;

    @Autowired
    public ItemRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Item item){
        if (item.getId() == null){ //신규 등록
            em.persist(item);
        }else{  //업데이트
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
