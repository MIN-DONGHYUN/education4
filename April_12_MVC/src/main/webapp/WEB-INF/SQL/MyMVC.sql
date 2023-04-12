---- *** MyMVC 다이내믹웹프로젝트 에서 작업한 것 *** ----

---- **** === 오라클 계정 생성하기 시작 === **** ----

-- 오라클 계정 생성을 위해서는 SYS 또는 SYSTEM 으로 연결하여 작업을 해야 합니다. [SYS 시작] --
show user;
-- USER이(가) "SYS"입니다.

-- 오라클 계정 생성시 계정명 앞에 c## 붙이지 않고 생성하도록 하겠습니다.
alter session set "_ORACLE_SCRIPT"=true;
-- Session이(가) 변경되었습니다.

-- 오라클 계정명은 MyMVC_USER 이고 암호는 gclass 인 사용자 계정을 생성합니다.
create user MyMVC_USER identified by gclass default tablespace users; 
-- User MYMVC_USER이(가) 생성되었습니다.

-- 위에서 생성되어진 MyMVC_USER 이라는 오라클 일반사용자 계정에게 오라클 서버에 접속이 되어지고,
-- 테이블 생성 등등을 할 수 있도록 여러가지 권한을 부여해주겠습니다.
grant connect, resource, create view, unlimited tablespace to MyMVC_USER;
-- Grant을(를) 성공했습니다.

---- **** === 오라클 계정 생성하기 끝 === **** ----


show user;
-- USER이(가) "MYMVC_USER"입니다.

-- 테이블 만들기
create table tbl_main_image
(imgno           number not null
,imgfilename     varchar2(100) not null
,constraint PK_tbl_main_image primary key(imgno)
);
-- Table TBL_MAIN_IMAGE이(가) 생성되었습니다.

-- 시퀀스 만들기
create sequence seq_main_image
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_MAIN_IMAGE이(가) 생성되었습니다.


insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '미샤.png');  
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '원더플레이스.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '레노보.png'); 
insert into tbl_main_image(imgno, imgfilename) values(seq_main_image.nextval, '동원.png'); 


commit;
-- 커밋 완료.


select imgno, imgfilename
from tbl_main_image 
order by imgno asc;
-- 1	미샤.png
-- 2	원더플레이스.png
-- 3	레노보.png
-- 4	동원.png


----------------------------------------------------------


                -- *** 회원 테이블 생성 *** -- 
create table tbl_member
(userid             varchar2(40)   not null  -- 회원아이디
,pwd                varchar2(200)  not null  -- 비밀번호 (SHA-256 암호화 대상)
,name               varchar2(30)   not null  -- 회원명
,email              varchar2(200)  not null  -- 이메일 (AES-256 암호화/복호화 대상)
,mobile             varchar2(200)            -- 연락처 (AES-256 암호화/복호화 대상) 
,postcode           varchar2(5)              -- 우편번호
,address            varchar2(200)            -- 주소
,detailaddress      varchar2(200)            -- 상세주소
,extraaddress       varchar2(200)            -- 참고항목
,gender             varchar2(1)              -- 성별   남자:1  / 여자:2
,birthday           varchar2(10)             -- 생년월일   
,coin               number default 0         -- 코인액
,point              number default 0         -- 포인트 
,registerday        date default sysdate     -- 가입일자 
,lastpwdchangedate  date default sysdate     -- 마지막으로 암호를 변경한 날짜  
,status             number(1) default 1 not null     -- 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
,idle               number(1) default 0 not null     -- 휴면유무      0 : 활동중  /  1 : 휴면중 
,constraint PK_tbl_member_userid primary key(userid)
,constraint UQ_tbl_member_email  unique(email)
,constraint CK_tbl_member_gender check( gender in('1','2') )
,constraint CK_tbl_member_status check( status in(0,1) )
,constraint CK_tbl_member_idle check( idle in(0,1) )
);
-- Table TBL_MEMBER이(가) 생성되었습니다.


select * 
from tbl_member
order by registerday desc;


-- 테이블 생성 (로그인이 성공되어지면 자동적으로 로그인 기록을 남기려고 insert 되어질 테이블) --
create table tbl_loginhistory
(fk_userid   varchar2(40) not null 
,logindate   date default sysdate not null
,clientip    varchar2(20) not null
,constraint FK_tbl_loginhistory foreign key(fk_userid) 
                                references tbl_member(userid)  
);
-- Table TBL_LOGINHISTORY이(가) 생성되었습니다.

-- insert 넣을 곳
select *
from tbl_loginhistory
order by logindate desc;

insert into tbl_loginhistory(fk_userid, logindate, clientip)
values('eomjh',sysdate - 2, '127.0.0.1');

insert into tbl_loginhistory(fk_userid, logindate, clientip)
values('leess',sysdate - 1, '127.0.0.1');

insert into tbl_loginhistory(fk_userid, logindate, clientip)
values('leess',sysdate , '127.0.0.1');

insert into tbl_loginhistory(fk_userid, logindate, clientip)
values('emojh',sysdate , '127.0.0.1');



commit;


delete from tbl_loginhistory;
commit;

update tbl_loginhistory set logindate = add_months(logindate, -20);
commit;

-- 커밋 완료.

--- 1. 마지막으로 암호를 변경한 날짜가 현재일로 부터 3개월이 지났으면 암호를 변경해야 한다라는 메시지를 출력하도록 해야 한다. ---
--- 2. 마지막으로 로그인 한 날짜가 현재시각으로 부터 1년이 넘었으면 휴먼으로 지정해야 한다.
--- 3. 정상적으로 로그인 되었으면 로그인 사용자의 상세정보를 알아와야 한다.

select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender, 
       birthyyyy , birthmm, birthdd, coin, point, registerday, 
       pwdchangegap , 
       NVL(lastlogingap, trunc(months_Between(sysdate, registerday)))  AS lastlogingap     
from 
(
    select userid, name, email, mobile, postcode, address, detailaddress, extraaddress, gender
          , substr(birthday, 1 , 4) AS birthyyyy , substr(birthday, 6 , 2) AS birthmm , substr(birthday, 9) AS birthdd
          , coin, point, to_char(registerday, 'yyyy-mm-dd') AS registerday
          , trunc(months_between(sysdate, lastpwdchangedate), 0) AS pwdchangegap 
    from tbl_member
    where status = 1 and userid = 'leess' and pwd = '18006e2ca1c2129392c66d87334bd2452c572058d406b4e85f43c1f72def10f5'
) M                   -- months_between 는 개월수 차이를 알아보는 것이다.  trunc 는 정수부만 나오게 한다.  
CROSS JOIN
(                    --- 로그인 한지 얼마나 지났는지 알아보는 것 
    select trunc(months_Between(sysdate, max(logindate)),0) AS lastlogingap  -- , 0 은 뺄수 있다. 정수부까지이니까
    from tbl_loginhistory
    where fk_userid = 'leess'
) H


-- 로그인 날짜 변경
update tbl_member set registerday= add_months(registerday, -5)
        , lastpwdchangedate = add_months(lastpwdchangedate, -4)
where userid = 'hongkd';

commit;

-- IDLE 변경
update tbl_member set idle = 0
where userid = 'eomjh';

commit;

-- 강감찬을 강제로 휴먼처리 해보자 
update tbl_loginhistory set logindate = add_months(logindate, -13)
where fk_userid = 'kangkc';
-- 나머지 휴먼 처리 해제
update tbl_loginhistory set logindate = add_months(logindate, +20)
where fk_userid = 'eomjh';

commit;



-- 삭제하는 방법 
delete 
from tbl_loginhistory
where fk_userid = 'admin';

delete 
from tbl_member
where userid = 'admin';

commit;


-- tbl_loginhistory 보기 
select *
from tbl_loginhistory
order by logindate desc;


select * 
from tbl_member
order by registerday desc;