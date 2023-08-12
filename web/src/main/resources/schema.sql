alter table show_time
drop
foreign key FK_show_time;

alter table shows_introduction_images
drop
foreign key FK_shows_introduction_images;

drop table if exists account;
drop table if exists facility;
drop table if exists favorite_show;
drop table if exists images;
drop table if exists lost_item;
drop table if exists member;
drop table if exists party;
drop table if exists party_member;
drop table if exists show_review;
drop table if exists show_review_like;
drop table if exists show_time;
drop table if exists shows;
drop table if exists shows_introduction_images;


create table account
(
    use_yn                   bit          not null,
    account_id               bigint       not null auto_increment,
    created_at               datetime(6) not null,
    last_modified_at         datetime(6) not null,
    member_id                bigint,
    refresh_token_expires_at datetime(6) not null,
    password                 varchar(255),
    refresh_token            varchar(255) not null,
    username                 varchar(255) not null,
    primary key (account_id)
) engine=InnoDB;

create index IX_account__member
    on account (member_id);

alter table account
    add constraint UK_account__username unique (username);


create table facility
(
    hall_num         integer      not null,
    latitude         float(53)    not null,
    longitude        float(53)    not null,
    opening_year     integer      not null,
    seat_num         integer      not null,
    use_yn           bit          not null,
    created_at       datetime(6) not null,
    last_modified_at datetime(6) not null,
    facility_id      varchar(25)  not null,
    address          varchar(255) not null,
    characteristics  varchar(255) not null,
    homepage         varchar(255) not null,
    name             varchar(255) not null,
    phone            varchar(255) not null,
    primary key (facility_id)
) engine=InnoDB;


create table favorite_show
(
    favorite_show_id bigint      not null auto_increment,
    member_id        bigint      not null,
    show_id          varchar(25) not null,
    primary key (favorite_show_id)
) engine=InnoDB;

create index IX_favorite_show__show
    on favorite_show (show_id);

alter table favorite_show
    add constraint UK_favorite_show__member_show unique (member_id, show_id);


create table images
(
    use_yn           bit          not null,
    created_at       datetime(6) not null,
    created_by       bigint       not null,
    images_id        bigint       not null auto_increment,
    last_modified_at datetime(6) not null,
    origin_name      varchar(255) not null,
    stored_name      varchar(255) not null,
    url              varchar(255) not null,
    primary key (images_id)
) engine=InnoDB;


create table lost_item
(
    use_yn             bit          not null,
    created_at         datetime(6) not null,
    created_by         bigint       not null,
    found_at           datetime(6) not null,
    image_id           bigint       not null,
    last_modified_at   datetime(6) not null,
    lost_item_id       bigint       not null auto_increment,
    facility_id        varchar(25)  not null,
    found_place_detail varchar(255) not null,
    particulars        varchar(255) not null,
    title              varchar(255) not null,
    type               enum ('BAG','BOOK','CARD','CASH','CLOTHING','ELECTRONIC_EQUIPMENT','ETC','JEWELRY','WALLET') not null,
    primary key (lost_item_id)
) engine=InnoDB;

create index IX_lost_item__found_at
    on lost_item (found_at);

create index IX_lost_item__title_found_at
    on lost_item (title, found_at);

create index IX_lost_item__title_facility_found_at
    on lost_item (title, facility_id, found_at);

create index IX_lost_item__facility_found_at
    on lost_item (facility_id, found_at);

create index IX_lost_item__facility_type_found_at
    on lost_item (facility_id, type, found_at);


create table member
(
    use_yn           bit         not null,
    created_at       datetime(6) not null,
    image_id         bigint      not null,
    last_modified_at datetime(6) not null,
    member_id        bigint      not null auto_increment,
    nickname         varchar(25) not null,
    primary key (member_id)
) engine=InnoDB;

alter table member
    add constraint UK_member__nickname unique (nickname);


create table party
(
    closed           bit           not null,
    cur_member_num   integer       not null,
    max_member_num   integer       not null,
    use_yn           bit           not null,
    created_at       datetime(6) not null,
    created_by       bigint        not null,
    last_modified_at datetime(6) not null,
    party_id         bigint        not null auto_increment,
    show_at          datetime(6) not null,
    category         enum ('ETC','FOOD_CAFE','WATCHING') not null,
    show_id          varchar(25)   not null,
    content          varchar(2000) not null,
    title            varchar(255)  not null,
    primary key (party_id)
) engine=InnoDB;

create index IX_party__show
    on party (show_id);

create index IX_party__created_by
    on party (created_by);


create table party_member
(
    member_id    bigint not null,
    party_id     bigint not null,
    party_member bigint not null auto_increment,
    primary key (party_member)
) engine=InnoDB;

alter table party_member
    add constraint UK_party_member__party_id_member_id unique (member_id, party_id);


create table show_review
(
    grade            integer      not null,
    like_count       integer      not null,
    use_yn           bit          not null,
    created_at       datetime(6) not null,
    created_by       bigint       not null,
    last_modified_at datetime(6) not null,
    show_review_id   bigint       not null auto_increment,
    show_id          varchar(25)  not null,
    content          varchar(255) not null,
    primary key (show_review_id)
) engine=InnoDB;

create index IX_show_review__created_by
    on show_review (created_by);

create index IX_show_review__show_like_count
    on show_review (show_id, like_count);


create table show_review_like
(
    member_id           bigint not null,
    show_review_id      bigint not null,
    show_review_like_id bigint not null auto_increment,
    primary key (show_review_like_id)
) engine=InnoDB;

alter table show_review_like
    add constraint UK_show_review_like__member_show_review unique (member_id, show_review_id);


create table shows
(
    end_date         date          not null,
    review_count     integer       not null,
    start_date       date          not null,
    use_yn           bit           not null,
    created_at       datetime(6) not null,
    last_modified_at datetime(6) not null,
    review_grade_sum bigint        not null,
    facility_id      varchar(25)   not null,
    genre            enum ('MUSICAL','PLAY') not null,
    openrun          varchar(25)   not null,
    show_id          varchar(25)   not null,
    state            varchar(25)   not null,
    story            varchar(1000) not null,
    age              varchar(255)  not null,
    cast             varchar(255)  not null,
    crew             varchar(255)  not null,
    enterprise       varchar(255)  not null,
    name             varchar(255)  not null,
    poster           varchar(255)  not null,
    runtime          varchar(255)  not null,
    ticket_price     varchar(255)  not null,
    primary key (show_id)
) engine=InnoDB;

create index IX_show__facility
    on shows (facility_id);

create index IX_show__name
    on shows (name);

create index IX_show__start_date
    on shows (start_date);

create index IX_show__end_date
    on shows (end_date);

create index IX_show__genre
    on shows (genre);


create table show_time
(
    time        time(6)     not null,
    day_of_week enum ('FRIDAY','MONDAY','SATURDAY','SUNDAY','THURSDAY','TUESDAY','WEDNESDAY') not null,
    show_id     varchar(25) not null
) engine=InnoDB;

alter table show_time
    add constraint FK_show_time
        foreign key (show_id)
            references shows (show_id);


create table shows_introduction_images
(
    show_id             varchar(25) not null,
    introduction_images varchar(255)
) engine=InnoDB;

alter table shows_introduction_images
    add constraint FK_shows_introduction_images
        foreign key (show_id)
            references shows (show_id);