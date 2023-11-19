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

@Slf4j
@RequiredArgsConstructor
public class JpaRemoveItemWriter<T> implements ItemWriter<T>, InitializingBean {

    private final EntityManagerFactory entityManagerFactory;

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

    protected void doWrite(EntityManager entityManager, Chunk<? extends T> items) {

        if (log.isDebugEnabled()) {
            log.debug("Writing to JPA with " + items.size() + " items.");
        }

        if (!items.isEmpty()) {
            long addedToContextCount = 0;
            for (T item : items) {
                entityManager.remove(item);
                addedToContextCount++;
            }
            if (log.isDebugEnabled()) {
                log.debug(addedToContextCount + "entities removed.");
            }
        }

    }
}
