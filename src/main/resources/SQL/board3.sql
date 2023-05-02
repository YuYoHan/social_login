create database board3;
use board3;

create table user
(
    user_email       varchar(50) primary key,
    user_password    varchar(20) not null,
    user_nickName    varchar(30) not null,
    user_phoneNumber varchar(15) not null,
    user_address     TEXT        not null,
    user_profile     TEXT
);

create table board
(
    board_number          int primary key auto_increment,
    board_title           varchar(200) not null,
    board_content         TEXT         not null,
    board_image           text,
    board_video           text,
    board_file            text,
    board_writer_email    varchar(50)  not null,
    board_writer_profile  text,
    board_writer_nickName varchar(30)  not null,
    board_write_date      DATE         not null,
    board_click_count     int default 0,
    board_like_count      int default 0,
    board_comment_count   int default 0
);

create table popularSearch
(
    popular_term         varchar(300) primary key,
    popular_search_count int default 1
);

create table liky
(
    like_id            int auto_increment primary key,
    board_number       int         not null,
    user_email         varchar(50) not null,
    like_user_profile  text,
    like_user_nickName varchar(30) not null

);
drop table liky;

create table comment
(
    comment_id            int auto_increment primary key,
    board_number          int         not null,
    user_email            varchar(50) not null,
    comment_user_profile  text,
    comment_user_nickName varchar(30) not null,
    comment_write_date    DATE        NOT NULL,
    comment_content       text        not null
);
drop table comment;