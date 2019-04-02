drop table creatureTemplate;
drop table creature;
drop table monster;
drop table weapon;
drop table creature_weapon;
drop table users;
drop table player;
drop table initiative;
drop table game_lobby;
drop table chat_message;


-- create tables

create table creatureTemplate(
    creature_id INTEGER NOT NULL AUTO_INCREMENT,
    creature_name VARCHAR(30),
    hp INTEGER,
    ac INTEGER,
    movement INTEGER,
    damage_bonus INTEGER,
    attack_bonus INTEGER,
    attacks_per_turn INTEGER,
    backstory TEXT,
    image_url VARCHAR(30),
    playable BOOLEAN,
    constraint creatureTemplate_pk primary key(creature_id));

create table creature(
    player_id integer not null,
    creature_id integer not null,
    creature_name VARCHAR(30),
    HP smallint,
    AC smallint,
    movement integer,
    damage_bonus integer,
    attack_bonus integer,
    attacks_per_turn integer,
    backstory TEXT,
    pos_x integer,
    pos_y integer,
    image_url VARCHAR(30),
    constraint creature_pk primary key(player_id));


--create table monster(
--    monster_id integer not null references creature(creature_id),
--    monster_lv integer not null,
--    monster_name varchar(20),
--    constraint monster_pk primary key(monster_id));

Create TABLE level(
    level_id integer,
    music varchar(30),
    background_uml varchar(30),
    constraint level_pk primary key (level_id));

    create table weapon(
    weapon_id integer,
    weapon_name varchar(20),
    dice_amount integer,
    damage_dice integer,
    description varchar(50),
    image_url varchar(20),
    CONSTRAINT weapon_pk primary key (weapon_id));


create table creature_weapon(
    weapon_id integer not null,
    creature_id integer not null,
    constraint creature_weapon_pk primary key(weapon_id, creature_id),
alter table creature_weapon add CONSTRAINT creature_weapon_fk1 FOREIGN KEY(weapon_id)REFERENCES weapon(weapon_id);,
alter table creature_weapon add CONSTRAINT creature_weapon_fk2 FOREIGN KEY(creature_id)REFERENCES creatureTemplate(creature_id);



create table creature_weapon(
    weapon_id integer not null,
    creature_id integer not null,
    constraint creature_weapon_pk primary key(weapon_id, creature_id));


create table usr(
    user_id integer not null AUTO_INCREMENT,
    username varchar(30),
    rank integer default 0,
    password char(64),
    lobby_key INTEGER,
    constraint usr_pk primary key(user_id));

create table password(
    user_id integer not null references usr(user_id),
    salt_pass char(64) not null,
    hash_pass text not null,
    constraint password_pk primary key(user_id));


create table player(
    player_id integer not null AUTO_INCREMENT,
    lobby_key INTEGER NOT NULL,
    user_id integer,
    ready boolean DEFAULT FALSE,
    constraint player_pk primary key(player_id));

--create table initiative(
--    lobby_key integer not null,
--    player_id integer not null,
--    initiative_turn integer,
--    constraint initiative_pk primary key(lobby_key, user_id));

CREATE TABLE level_monster(
    level_id integer not null,
    creature_id integer not null,
    constraint level_monster_pk primary key(level_id,creature_id));

    ALTER TABLE level_monster ADD CONSTRAINT level_monster_fk1 FOREIGN KEY(creature_id) REFERENCES creatureTemplate(creature_id);
    ALTER TABLE level_monster ADD CONSTRAINT level_monster_fk2 FOREIGN KEY (level_id) REFERENCES level(level_id);

create table game_lobby(
    lobby_key integer not null AUTO_INCREMENT,
    player_turn integer,
    level_id integer,
    joinable boolean DEFAULT TRUE,
    constraint game_lobby_pk primary key(lobby_key));

create table chat_message(
    lobby_key integer not null,
    message_id integer not null AUTO_INCREMENT,
    user_id INTEGER,
    message TEXT,
    time_stamp TIME NOT NULL,
    constraint chat_message_pk primary key(message_id, lobby_key));

CREATE VIEW user_message AS
SELECT
    lobby_key,
    message_id,
    user_id,
    message,
    time_stamp
    FROM chat_message
    WHERE user_id IS NOT NULL;

CREATE VIEW event_message AS
SELECT
    lobby_key,
    message_id,
    message,
    time_stamp
    FROM chat_message
    WHERE user_id IS NULL;



ALTER TABLE user ADD CONSTRAINT user_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);

ALTER TABLE chat_message ADD CONSTRAINT chat_message_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);
ALTER TABLE chat_message ADD CONSTRAINT chat_message_fk2 FOREIGN KEY(user_id) REFERENCES user(user_id);

ALTER TABLE player ADD CONSTRAINT player_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);
ALTER TABLE player ADD CONSTRAINT player_fk2 FOREIGN KEY(user_id) REFERENCES usr(user_id);

ALTER TABLE creature ADD CONSTRAINT creature_fk1 FOREIGN KEY(player_id) REFERENCES player(player_id);
ALTER TABLE creature ADD CONSTRAINT creature_fk2 FOREIGN KEY(creature_id) REFERENCES creatureTemplate(creature_id);

ALTER TABLE creature_weapon ADD CONSTRAINT creature_weapon_fk1 FOREIGN KEY(weapon_id)REFERENCES weapon(weapon_id);
ALTER TABLE creature_weapon ADD CONSTRAINT creature_weapon_fk2 FOREIGN KEY(creature_id)REFERENCES creatureTemplate(creature_id);

--ALTER TABLE initiative ADD CONSTRAINT initiative_fk1 FOREIGN KEY(player_id, lobby_key) REFERENCES player(player_id, lobby_key);
--ALTER TABLE initiative ADD CONSTRAINT initiative_fk2 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);


-- INSERTS
INSERT INTO creatureTemplate VALUES(DEFAULT, "Warrior", 36, 18, 3, 5, 8, 2, "He be legendary warior. Yeet that goblin", "warrior.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Rogue", 23, 16, 3, 7, 7, 2, "she be the sneaky girl. not the one from Rogue One", "rogue.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Wizard", 22, 15, 3, 0, 8, 1, "Penny for your thoughts. Nothing that a little music can't help. Seagulls stop it now.", "wizard.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Ranger", 32, 16, 3, 5, 9, 2, "Think LOtR. pointy ears, just as sexy", "ranger.jpg", true);
INSERT INTO creatureTemplate(creature_id,creature_name,hp,ac,movement,damage_bonus,attack_bonus,attacks_per_turn) values(5,"Hell Hound",45,15,5,3,5,1, "judge.jpg", false)
INSERT INTO creatureTemplate(creature_id,creature_name,hp,ac,movement,damage_bonus,attack_bonus,attacks_per_turn) values(6,"Earth elemental",126,17,3,5,8,2, false);
INSERT INTO creatureTemplate(creature_id,creature_name,hp,ac,movement,damage_bonus,attack_bonus,attacks_per_turn) values(7,"Grick alpha",75,18,3,4,7,2, false);
INSERT INTO creatureTemplate(creature_id,creature_name,hp,ac,movement,damage_bonus,attack_bonus,attacks_per_turn) values(8,"Green slaad",127,16,3,4,7,3, false);
INSERT INTO creatureTemplate(creature_id,creature_name,hp,ac,movement,damage_bonus,attack_bonus,attacks_per_turn) values(9,"Dragon",178,18,4,6,10,3, false);

--weapon
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (1,"Longsword",1,8,"Melee","Long_sword.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (2,"Javelin",1,6,"Ranged","Javelin.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (3,"Longbow",1,8,"Ranged","Long_bow.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (4, "Short sword", 1,6,"Melee","Short_sword.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (5,"Dagger",1,4,"Melee","Dagger.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (6,"Hand crossbow",1,6,"Ranged","Crossbow.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (7,"Chromatic orb",3,8,"Ranged","Chromatic_orb.png");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description,weapon_url) VALUES (8,"Fireball",6,6,"Ranged","Fireball.png");

INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (9,"Slaad bite",2,6,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (10,"Slaad claw",1,6,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (11,"Slaad hurl flame",3,6,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (12,"Elemental slam",2,8,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (13,"Grick tail",2,6,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (14,"Grick tentacles",4,8,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (15, "Grick break",2,8,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (16,"Hound bite", 1,8,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (17,"Dragon bite",2,10,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (18,"Dragon claw",2,6,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (19,"Dragon breath",6,10,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (20,"Hound breath",2,6,"Ranged");


--weapon and creature
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (1,1);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (2,1);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (3,4);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (4,4);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (5,2);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (6,2);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (7,3);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (8,3);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (9,8);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (10,8);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (11,8);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (12,6);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (13,7);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (14,7);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (15,7);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (16,5);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (17,9);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (18,9);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (19,9);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (20,5);

