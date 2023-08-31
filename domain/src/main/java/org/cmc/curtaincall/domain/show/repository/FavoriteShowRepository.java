package org.cmc.curtaincall.domain.show.repository;

import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.show.FavoriteShow;
import org.cmc.curtaincall.domain.show.Show;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FavoriteShowRepository extends JpaRepository<FavoriteShow, Long> {

    boolean existsByMemberAndShow(Member member, Show show);

    Optional<FavoriteShow> findByMemberAndShow(Member member, Show show);

    List<FavoriteShow> findAllByMemberAndShowIn(Member member, Collection<Show> shows);

    @EntityGraph(attributePaths = {"show", "show.facility"})
    Slice<FavoriteShow> findSliceWithShowByMember(Pageable pageable, Member member);
}
