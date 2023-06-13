create database board3;
use board3;

use board3;

create table user(
            userId bigint primary key auto_increment ,
            userEmail varchar(300) unique ,
            userPw varchar(300),
            userName varchar(300) not null,
            provider varchar(1000),
            providerId varchar(1000)
);

create table board(
                      boardNum bigint primary key auto_increment,
                      boardTitle varchar(300) not null ,
                      boardContents varchar(6000) ,
                      userId bigint,
                      regDate datetime default now(),
                      updateDate datetime default now(),
                      constraint board_id_fk foreign key (userId) references user(userId)
);

select * from board;
select * from user;
drop table board;
drop table user;

insert into board (boardTitle, boardContents)
values	('테스트 제목', 'apple이 작성한 테스트 내용'),
          ('테스트 제목', 'banana이 작성한 테스트 내용'),
          ('테스트 제목', 'cheery이 작성한 테스트 내용'),
          ('테스트 제목', 'durian이 작성한 테스트 내용');
# 가상의 게시글 늘리기 위한 것
insert into board (boardTitle, boardContents) (select boardTitle, boardContents from board);


