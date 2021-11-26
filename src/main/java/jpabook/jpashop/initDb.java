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
public class initDb {

    private final InitService initService;


    @PostConstruct
    public void init(){

        initService.dbInit1();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final EntityManager em;

        public void dbInit1(){
            Member member = createMember("서울", "1", "1111", "userA");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            OrderItem item1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem item2 = OrderItem.createOrderItem(book2, 20000, 1);


            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, item1, item2);

            em.persist(order);
        }

        public void dbInit2(){

            Member member = createMember("진주", "2", "2222", "userB");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem item1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem item2 = OrderItem.createOrderItem(book2, 40000, 4);


            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, item1, item2);

            em.persist(order);
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Member createMember(String city, String street, String zipcode, String name) {
            Member member = new Member();
            member.setAddress(new Address(city, street, zipcode));
            member.setName(name);
            return member;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }

}
