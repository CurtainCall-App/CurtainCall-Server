package org.cmc.curtaincall.web.code;

import org.cmc.curtaincall.domain.member.MemberWithdrawReason;
import org.cmc.curtaincall.domain.report.ReportReason;
import org.cmc.curtaincall.domain.report.ReportType;
import org.cmc.curtaincall.domain.show.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnumMapperConfig {

    /**
     *  아래와 같이 EnumMapperType 을 구현한 enum 을 enumMapperFactory 에 등록합니다.
     *  enumMapperFactory.put(EnumCodeExample 을 등록할 이름, EnumCodeExample.class);
     */
    @Bean
    public EnumMapperFactory enumMapperFactory() {
        EnumMapperFactory enumMapperFactory = new EnumMapperFactory();
        enumMapperFactory.put(ReportReason.class.getSimpleName(), ReportReason.class);
        enumMapperFactory.put(BoxOfficeGenre.class.getSimpleName(), BoxOfficeGenre.class);
        enumMapperFactory.put(BoxOfficeType.class.getSimpleName(), BoxOfficeType.class);
        enumMapperFactory.put(ShowDay.class.getSimpleName(), ShowDay.class);
        enumMapperFactory.put(ShowGenre.class.getSimpleName(), ShowGenre.class);
        enumMapperFactory.put(MemberWithdrawReason.class.getSimpleName(), MemberWithdrawReason.class);
        enumMapperFactory.put(ShowState.class.getSimpleName(), ShowState.class);
        enumMapperFactory.put(ReportType.class.getSimpleName(), ReportType.class);
        return enumMapperFactory;
    }
}
