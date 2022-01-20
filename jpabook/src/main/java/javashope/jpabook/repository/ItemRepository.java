package javashope.jpabook.repository;

import javashope.jpabook.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 처음 item 객체를 생성하면 db에 저장되기 전까지 ID를 할당받지 못한다
     * 때문에 item의 id가 null이면 db에 저장
     * item의 id가 이미 db에 있다면 merge
     * @param item
     */
    public void sava(Item item){
        if (item.getId() == null) {
            em.persist(item);
        } else{
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
