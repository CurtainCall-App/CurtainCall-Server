package org.cmc.curtaincall.web.service.member;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.member.request.MemberDelete;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDeleteService {

    private final EntityManager em;

    @Transactional
    public void delete(Long memberId, MemberDelete memberDelete) {
        em.createQuery("""
                        update Member member
                        set member.useYn = false
                        where member.id = :memberId
                        """)
                .setParameter("memberId", memberId)
                .executeUpdate();
        em.createQuery("""
                        update LostItem lostItem
                        set lostItem.useYn = false
                        where lostItem.createdBy.id = :memberId
                        """)
                .setParameter("memberId", memberId)
                .executeUpdate();
        em.createQuery("""
                        update Image image
                        set image.useYn = false
                        where image.createdBy.id = :memberId
                        """)
                .setParameter("memberId", memberId)
                .executeUpdate();
        em.createQuery("""
                        update Image image
                        set image.useYn = false
                        where image.createdBy.id = :memberId
                        """)
                .setParameter("memberId", memberId)
                .executeUpdate();
    }
}
