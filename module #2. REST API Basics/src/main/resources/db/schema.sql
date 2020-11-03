create table if not exists giftcertificate
(
    id             bigint auto_increment
        primary key,
    name           varchar(40)   not null,
    description    varchar(255)  null,
    price          decimal(5, 2) not null,
    create_date     datetime        not null,
    last_update_date datetime        not null,
    duration_days       bigint        null
);
create table if not exists tag
(
    id   bigint auto_increment
        primary key,
    name varchar(20) not null,
    constraint tag_name_uindex
        unique (Name)
);
create table if not exists tag_certificate
(
    id            bigint auto_increment
        primary key,
    tag_id         bigint not null,
    certificate_id bigint not null,
    constraint sert_fk
        foreign key (certificate_id) references giftcertificate (id)
            on update cascade on delete cascade,
    constraint tag_fk
        foreign key (tag_id) references tag (id)
            on update cascade on delete cascade
);

