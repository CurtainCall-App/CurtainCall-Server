insert into facility
(facility_id,
 name,
 hall_num,
 characteristics,
 opening_year,
 seat_num,
 latitude,
 longitude,
 homepage,
 address,
 phone,
 created_at,
 last_modified_at,
 use_yn)
values ('FC000001',
        '예술의전당',
        11,
        '국립',
        1988,
        164396,
        37.4786896,
        127.01182410000001,
        'http://www.sac.or.kr',
        '서울특별시 서초구 남부순환로 2406 (서초동)',
        '02-580-1300',
        now(),
        now(),
        true);

insert into shows
(show_id,
 facility_id,
 name,
 start_date,
 end_date,
 cast,
 crew,
 runtime,
 age,
 enterprise,
 ticket_price,
 poster,
 genre,
 state,
 openrun,
 story,
 review_count,
 review_grade_sum,
 created_at,
 last_modified_at,
 use_yn)
values ('PF218232',
        'FC000001',
        '그날들',
        '2023-07-12',
        '2023-09-03',
        '유준상, 이건명, 오만석, 엄기준, 오종혁, 지창욱, 김지현 등',
        '정우재, 정경진, 장유정 등',
        '2시간 45분',
        '만 7세 이상',
        '',
        'OP석 160,000원, R석 160,000원, S석 130,000원, A석 100,000원, B석 70,000원, C석 50,000원',
        'http://www.kopis.or.kr/upload/pfmPoster/PF_PF218232_230510_162549.jpg',
        'MUSICAL',
        '공연중',
        'N',
        '',
        0,
        0,
        now(),
        now(),
        true);

insert into show_time (show_id, time, day_of_week)
values
    ('PF218232', '19:30:00', 'TUESDAY'),
    ('PF218232', '14:30:00', 'WEDNESDAY'),
    ('PF218232', '19:30:00', 'WEDNESDAY')
;

insert into shows_introduction_images (show_id, introduction_images)
values
    ('PF218232', 'http://www.kopis.or.kr/upload/pfmIntroImage/PF_PF218232_230510_0425490.jpg'),
    ('PF218232', 'http://www.kopis.or.kr/upload/pfmIntroImage/PF_PF218232_230510_0426212.jpg')
;