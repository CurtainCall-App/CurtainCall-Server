package org.cmc.curtaincall.web.member;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberEditor;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.member.request.MemberEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final ImageRepository imageRepository;

    @Transactional
    public void edit(Long memberId, MemberEdit memberEdit) {
        Member member = getMemberById(memberId);
        MemberEditor.MemberEditorBuilder editorBuilder = member.toEditor()
                .nickname(memberEdit.getNickname());
        if (!isImageIdEqual(member.getImage(), memberEdit.getImageId())) {
            Optional.ofNullable(member.getImage())
                    .ifPresent(Image::delete);
            Image imageToEdit = Optional.ofNullable(memberEdit.getImageId())
                    .map(this::getImageById)
                    .orElse(null);
            editorBuilder.image(imageToEdit);
        }

        member.edit(editorBuilder.build());
    }

    private boolean isImageIdEqual(@Nullable Image image, @Nullable Long imageId) {
        Long id = Optional.ofNullable(image)
                .map(Image::getId)
                .orElse(null);
        return Objects.equals(id, imageId);
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .filter(Member::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Member id=" + id));
    }

    private Image getImageById(Long id) {
        return imageRepository.findById(id)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image id=" + id));
    }

}
