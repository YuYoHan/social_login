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
                      userEmail varchar(300) not null,
                      hashTag varchar(300),
                      regDate datetime default now(),
                      updateDate datetime default now(),
                      constraint board_id_fk foreign key (userId) references user(userId)
);

create table boardImg(
                         boardImgNum bigint primary key auto_increment,
                         boardImg varchar(3000),
                         boardNum bigint, foreign key pk_boardNum(boardNum) references board(boardNum)
);

create table comment(
                        commentNum bigint primary key auto_increment,
                        comment varchar(3000),
                        userId bigint, foreign key pk_userId3(userId) references user(userId),
                        boardNum bigint, foreign key pk_boardNum(boardNum) references board(boardNum),
                        commentTime timestamp not null default current_timestamp on update current_timestamp
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


