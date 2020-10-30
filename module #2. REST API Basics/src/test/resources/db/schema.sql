create table if not exists giftcertificate
(
    Id             bigint auto_increment
        primary key,
    Name           varchar(40)   not null,
    Description    varchar(255)  null,
    Price          decimal(5, 2) not null,
    CreateDate     bigint        not null,
    LastUpdateDate bigint        not null,
    Duration       bigint        null
);
create table if not exists tag
(
    Id   bigint auto_increment
        primary key,
    Name varchar(255) not null,
    constraint tag_Name_uindex
        unique (Name)
);
create table if not exists tag_certificate
(
    Id            bigint auto_increment
        primary key,
    TagId         bigint not null,
    CertificateId bigint not null,
    constraint sert_fk
        foreign key (CertificateId) references giftcertificate (Id)
            on update cascade on delete cascade,
    constraint tag_fk
        foreign key (TagId) references tag (Id)
            on update cascade on delete cascade
);

