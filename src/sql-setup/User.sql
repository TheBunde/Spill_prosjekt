drop table usr;

create table usr(
    user_id integer not null auto_increment,
    username varchar(30),
    rank integer default 0,
    lv integer default 0,
    email varchar(30),
    password char(64),
    constraint user_pk primary key(user_id)
    );

-- insert data for test

insert into usr values(1, 'saramohammadi' , default, default, 'saramohammadi.sm17@gmail.com', 'Sr12345');

