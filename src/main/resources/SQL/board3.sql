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

insert into board (boardTitle, boardContents, userId)
values	('테스트 제목1', 'apple이 작성한 테스트 내용1', 'apple'),
          ('테스트 제목2', 'banana이 작성한 테스트 내용2', 'banana'),
          ('테스트 제목3', 'cheery이 작성한 테스트 내용3', 'cheery'),
          ('테스트 제목4', 'durian이 작성한 테스트 내용4', 'durian');

insert into board (boardTitle, boardContents, userId) (select boardTitle, boardContents,userId from board);
