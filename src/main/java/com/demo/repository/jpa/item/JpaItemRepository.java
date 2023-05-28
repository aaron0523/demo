// JpaItemRepository.java
package com.demo.repository.jpa.item;

import com.demo.domain.item.Item;
import com.demo.repository.support.ItemRepository;
import com.demo.repository.support.QuerydslRepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.demo.domain.item.QItem.item;

@Repository
public class JpaItemRepository extends QuerydslRepositorySupport implements ItemRepository {

    private final SpringDataJpaItemRepository jpaItemRepository;

    public JpaItemRepository(SpringDataJpaItemRepository jpaItemRepository) {
        super(Item.class);
        this.jpaItemRepository = jpaItemRepository;
    }

    public void save(Item item) {
        if (item.getId() == null) {
            getEntityManager().persist(item);
        } else {
            getEntityManager().merge(item); // 업데이트
        }
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(getEntityManager().find(Item.class, id));
    }

    public List<Item> findAll() {
        return selectFrom(item).fetch();
    }

    public void delete(Item item) {
        getEntityManager().remove(item);
    }

    public Page<Item> getPaginatedItems(Pageable pageable) {
        return jpaItemRepository.findAll(pageable);
    }
}
