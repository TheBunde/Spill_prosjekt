drop table creature;
drop table classes;
drop table monster;
drop table weapon;
drop table users;
drop table player;
drop table initiative;
drop table game_lobby;
drop table chat;
drop table chat_message;


-- create tables

create table creature(
    creature_id integer not null,
    HP smallint,
    AC smallint,
    pos_x integer,
    pos_y integer,
    movement integer,
    attack_bonus integer,
    weapon_id not null,
    constraint creature_pk primary key(creature_id));

create table character(
    character_id integer not null references creature(creature_id),
    character_name varchar(20),
    back_story varchar(50),
    constraint classes_pk primary key(class_id));

create table monster(
    monster_id integer not null references creature(creature_id),
    monster_lv integer not null,
    monster_name varchar(20),
    constraint monster_pk primary key(monster_id));

create table weapon(
    weapon_id integer not null,
    weapon_name varchar(20),
    damage_dice smallint,
    damage_bonus integer,
    description varchar(50),
    constraint weapon_pk primary key(weapon_id));

create table user(
    user_id integer not null,
    username varchar(30),
    rank integer default 0,
    email varchar(30),
    password char(40),
    constraint user_pk primary key(user_id));

create table player(
    player_id integer not null references user(user_id),
    level integer,
    constraint player_pk primary key(player_id));

create table initiative(
    lobby_key integer not null references game_lobby(lobby_key),
    user_id integer not null references users(user_id),
    initiative_turn integer,
    constraint initiative_pk primary key(lobby_key, user_id));

create table game_lobby(
    lobby_key integer not null,
    player_turn integer,
    constraint game_lobby_pk primary key(lobby_key));

create table chat(
    chat_id integer not null,
    constraint chat_pk primary key(chat_id));

create table chat_message(
    chat_id integer not null references chat(chat_id),
    message_id integer not null,
    message varchar(50),
    constraint chat_message_pk primary key(chat_id, message_id));


    //Foreign keys

