create database board3;
use board3;

use board3;


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
