package org.cmc.curtaincall.batch.job.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
public class JpaQueryUpdateItemWriter<T, U> implements ItemWriter<T>, InitializingBean {

    private final EntityManagerFactory entityManagerFactory;

    private final BiFunction<EntityManager, T, U> query;

    private final BiConsumer<U, T> update;

    @Override
    public void write(final Chunk<? extends T> items) throws Exception {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
        }
        doWrite(entityManager, items);
        entityManager.flush();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(entityManagerFactory != null, "An EntityManagerFactory is required");
    }

    private void doWrite(EntityManager entityManager, Chunk<? extends T> items) {

        if (log.isDebugEnabled()) {
            log.debug("Update to JPA with " + items.size() + " items.");
        }

        if (!items.isEmpty()) {
            long addedToContextCount = 0;
            for (T item : items) {

                final U entity = query.apply(entityManager, item);
                update.accept(entity, item);

                addedToContextCount++;
            }
            if (log.isDebugEnabled()) {
                log.debug(addedToContextCount + "entities updated.");
            }
        }
    }

}
