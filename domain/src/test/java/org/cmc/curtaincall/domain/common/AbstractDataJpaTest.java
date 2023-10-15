package org.cmc.curtaincall.domain.common;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestJpaConfig.class)
public abstract class AbstractDataJpaTest {

    @Autowired
    protected EntityManager em;
}
