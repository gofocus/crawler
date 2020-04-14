create table LINK_POOL
(
    LINK      VARCHAR(500)          not null,
    PROCESSED BOOLEAN default FALSE not null,
    ID        INT auto_increment,
    constraint LINK_POOL_PK
        primary key (ID)
);

create table NEWS
(
    ID          INT auto_increment,
    TITLE       VARCHAR(200),
    CONTENT     TEXT,
    CREATED_AT  DATETIME,
    MODIFIED_AT DATETIME,
    constraint NEWS_PK
        primary key (ID)
);

