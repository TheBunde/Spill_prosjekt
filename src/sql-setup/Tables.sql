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
    creature_name VARCHAR(20),
    hp INTEGER,
    ac INTEGER,
    movement INTEGER,
    damage_bonus INTEGER,
    attack_bonus INTEGER,
    attacks_per_turn INTEGER,
    backstory TEXT,
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
    constraint creature_pk primary key(player_id));

--create table monster(
--    monster_id integer not null references creature(creature_id),
--   monster_lv integer not null,
-- monster_name varchar(20),
--constraint monster_pk primary key(monster_id));


create table creature_weapon(
    weapon_id integer not null,
    creature_id integer not null,
    constraint creature_weapon_pk primary key(weapon_id, creature_id));


create table usr(
    user_id integer not null AUTO_INCREMENT,
    username varchar(30),
    rank integer default 0,
    email varchar(30),
    password char(64),
    lobby_key INTEGER,
    constraint usr_pk primary key(user_id));

create table player(
    player_id integer not null AUTO_INCREMENT,
    lobby_key INTEGER NOT NULL,
    user_id integer,
    constraint player_pk primary key(player_id));

--create table initiative(
--    lobby_key integer not null,
--    player_id integer not null,
--    initiative_turn integer,
--    constraint initiative_pk primary key(lobby_key, user_id));

create table game_lobby(
    lobby_key integer not null AUTO_INCREMENT,
    player_turn integer,
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
INSERT INTO creatureTemplate VALUES(DEFAULT, "Warrior", 36, 18, 3, 5, 8, 2, "He be legendary warior. Yeet that goblin", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Rogue", 23, 16, 3, 7, 7, 2, "she be the sneaky girl. not the one from Rogue One", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Wizard", 22, 15, 3, 0, 8, 1, "Penny for your thoughts. Nothing that a little music can't help. Seagulls stop it now.", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Ranger", 32, 16, 3, 5, 9, 2, "Think LOtR. pointy ears, just as sexy", true);

INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (1,"Longsword",1,8,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (2,"Javelin",1,6,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (3,"Longbow",1,8,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (4, "Short sword", 1,6,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (5,"Dagger",1,4,"Melee");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (6,"Hand crossbow",1,6,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (7,"Chromatic orb",3,8,"Ranged");
INSERT INTO weapon (weapon_id, weapon_name, dice_amount, damage_dice, description) VALUES (8,"Fireball",6,6,"Ranged");
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

INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (1,1);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (2,1);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (5,2);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (6,2);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (7,3);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (8,3);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (5,4);
INSERT INTO creature_weapon (weapon_id,creature_id) VALUES (4,4);