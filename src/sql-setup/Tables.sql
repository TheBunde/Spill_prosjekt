drop table creature;
drop table player;
DROP TABLE level_monster;
drop table creature_weapon;
drop TABLE level;
drop table creatureTemplate;
drop table weapon;
drop table usr;
drop table initiative;
drop table game_lobby;
drop table chat_message;


-- create tables
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

create table game_lobby(
    lobby_key integer not null AUTO_INCREMENT,
    player_turn integer,
    level_id integer,
    joinable boolean DEFAULT TRUE,
    battlefield_ready BOOLEAN DEFAULT FALSE,
    constraint game_lobby_pk primary key(lobby_key));

create table chat_message(
    lobby_key integer not null,
    message_id integer not null AUTO_INCREMENT,
    user_id INTEGER,
    message TEXT,
    time_stamp TIME NOT NULL,
    constraint chat_message_pk primary key(message_id, lobby_key));

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

Create TABLE level(
    level_id integer AUTO_INCREMENT,
    music integer,
    background_url varchar(30),
    constraint level_pk primary key (level_id));

create table weapon(
    weapon_id integer AUTO_INCREMENT,
    weapon_name varchar(20),
    dice_amount integer,
    damage_dice integer,
    description varchar(50),
    image_url varchar(20),
    CONSTRAINT weapon_pk primary key (weapon_id));


create table creature_weapon(
    weapon_id integer not null,
    creature_id integer not null,
    constraint creature_weapon_pk primary key(weapon_id, creature_id));


create table player(
    player_id integer not null AUTO_INCREMENT,
    lobby_key INTEGER NOT NULL,
    user_id integer,
    ready boolean DEFAULT FALSE,
    ready_for_new_level BOOLEAN DEFAULT FALSE,
    constraint player_pk primary key(player_id));


CREATE TABLE level_monster(
    level_monster_id INTEGER AUTO_INCREMENT,
    level_id INTEGER NOT NULL,
    player_amount INTEGER NOT NULL,
    creature_id INTEGER NOT NULL,
    constraint level_monster_pk primary key(level_monster_id));


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



ALTER TABLE usr ADD CONSTRAINT user_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);

ALTER TABLE chat_message ADD CONSTRAINT chat_message_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);
ALTER TABLE chat_message ADD CONSTRAINT chat_message_fk2 FOREIGN KEY(user_id) REFERENCES usr(user_id);

ALTER TABLE player ADD CONSTRAINT player_fk1 FOREIGN KEY(lobby_key) REFERENCES game_lobby(lobby_key);
ALTER TABLE player ADD CONSTRAINT player_fk2 FOREIGN KEY(user_id) REFERENCES usr(user_id);

ALTER TABLE creature ADD CONSTRAINT creature_fk1 FOREIGN KEY(player_id) REFERENCES player(player_id);
ALTER TABLE creature ADD CONSTRAINT creature_fk2 FOREIGN KEY(creature_id) REFERENCES creatureTemplate(creature_id);

ALTER TABLE creature_weapon ADD CONSTRAINT creature_weapon_fk1 FOREIGN KEY(weapon_id)REFERENCES weapon(weapon_id);
ALTER TABLE creature_weapon ADD CONSTRAINT creature_weapon_fk2 FOREIGN KEY(creature_id)REFERENCES creatureTemplate(creature_id);

ALTER TABLE level_monster ADD CONSTRAINT level_monster_fk1 FOREIGN KEY(creature_id) REFERENCES creatureTemplate(creature_id);
ALTER TABLE level_monster ADD CONSTRAINT level_monster_fk2 FOREIGN KEY (level_id) REFERENCES level(level_id);

-- INSERTS
INSERT INTO level VALUES(1, 16, "Forest-map.png");
INSERT INTO level VALUES(2, 7, "Desert-map.png");
INSERT INTO level VALUES(3, 14, "Snow-map.png");
INSERT INTO level VALUES(4, 15, "Lava-map.png");
INSERT INTO level VALUES(5, 11, "Mountain-map.png");


INSERT INTO creatureTemplate VALUES(DEFAULT, "Warrior", 36, 18, 3, 5, 8, 2, "He be legendary warior. Yeet that goblin", "warrior.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Rogue", 23, 16, 3, 7, 7, 2, "she be the sneaky girl. not the one from Rogue One", "rogue.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Wizard", 22, 15, 3, 0, 8, 1, "Penny for your thoughts. Nothing that a little music can't help. Seagulls stop it now.", "wizard.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Ranger", 32, 16, 3, 5, 9, 2, "Think LOtR. pointy ears, just as sexy", "ranger.jpg", true);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Bear", 45, 15, 5, 3, 5, 1, NULL, "bear.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Ent", 50, 11, 3, 4, 1, 1, NULL, "Ent.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Mummy", 20, 15, 3, 3, 5, 1, NULL, "Mummi.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Ushabti", 20, 15, 3, 3, 5, 1, NULL, "Ushabti2.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Snow Wolf", 20, 15, 3, 3, 5, 1, NULL, "wolf.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Yeti", 20, 15, 3, 3, 5, 1, NULL, "yeti.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Lizard", 20, 15, 3, 3, 5, 1, NULL, "lizard.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Demon", 20, 15, 3, 3, 5, 1, NULL, "demon.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Dragon", 178, 18, 4, 6, 10, 3, NULL, "dragon.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Dragon", 267, 18, 4, 6, 10, 3, NULL, "dragon.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Dragon", 320, 18, 4, 6, 10, 3, NULL, "dragon.png", false);
INSERT INTO creatureTemplate VALUES(DEFAULT, "Dragon", 380, 18, 4, 6, 10, 3, NULL, "dragon.png", false);

-- Forest
INSERT INTO level_monster VALUES(DEFAULT, 1, 1, 5);

INSERT INTO level_monster VALUES(DEFAULT, 1, 2, 5);
INSERT INTO level_monster VALUES(DEFAULT, 1, 2, 6);

INSERT INTO level_monster VALUES(DEFAULT, 1, 3, 5);
INSERT INTO level_monster VALUES(DEFAULT, 1, 3, 5);
INSERT INTO level_monster VALUES(DEFAULT, 1, 3, 6);

INSERT INTO level_monster VALUES(DEFAULT, 1, 4, 5);
INSERT INTO level_monster VALUES(DEFAULT, 1, 4, 6);
INSERT INTO level_monster VALUES(DEFAULT, 1, 4, 6);

-- Desert
INSERT INTO level_monster VALUES(DEFAULT, 2, 1, 7);

INSERT INTO level_monster VALUES(DEFAULT, 2, 2, 8);

INSERT INTO level_monster VALUES(DEFAULT, 2, 3, 7);
INSERT INTO level_monster VALUES(DEFAULT, 2, 3, 7);
INSERT INTO level_monster VALUES(DEFAULT, 2, 3, 8);

INSERT INTO level_monster VALUES(DEFAULT, 2, 4, 7);
INSERT INTO level_monster VALUES(DEFAULT, 2, 4, 7);
INSERT INTO level_monster VALUES(DEFAULT, 2, 4, 7);
INSERT INTO level_monster VALUES(DEFAULT, 2, 4, 8);

-- Snow
INSERT INTO level_monster VALUES(DEFAULT, 3, 1, 10);

INSERT INTO level_monster VALUES(DEFAULT, 3, 2, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 2, 10);

INSERT INTO level_monster VALUES(DEFAULT, 3, 3, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 3, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 3, 10);

INSERT INTO level_monster VALUES(DEFAULT, 3, 4, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 4, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 4, 9);
INSERT INTO level_monster VALUES(DEFAULT, 3, 4, 10);
INSERT INTO level_monster VALUES(DEFAULT, 3, 4, 10);

-- Lava
INSERT INTO level_monster VALUES(DEFAULT, 4, 1, 11);
INSERT INTO level_monster VALUES(DEFAULT, 4, 1, 11);

INSERT INTO level_monster VALUES(DEFAULT, 4, 2, 11);
INSERT INTO level_monster VALUES(DEFAULT, 4, 2, 11);
INSERT INTO level_monster VALUES(DEFAULT, 4, 2, 12);

INSERT INTO level_monster VALUES(DEFAULT, 4, 3, 11);
INSERT INTO level_monster VALUES(DEFAULT, 4, 3, 12);
INSERT INTO level_monster VALUES(DEFAULT, 4, 3, 12);

INSERT INTO level_monster VALUES(DEFAULT, 4, 4, 11);
INSERT INTO level_monster VALUES(DEFAULT, 4, 4, 12);
INSERT INTO level_monster VALUES(DEFAULT, 4, 4, 12);
INSERT INTO level_monster VALUES(DEFAULT, 4, 4, 12);

-- Mountain
INSERT INTO level_monster VALUES(DEFAULT, 5, 1, 13);

INSERT INTO level_monster VALUES(DEFAULT, 5, 2, 14);

INSERT INTO level_monster VALUES(DEFAULT, 5, 3, 15);

INSERT INTO level_monster VALUES(DEFAULT, 5, 4, 16);


-- Weapons
INSERT INTO weapon VALUES (DEFAULT, "Longsword", 1, 8,"Melee", "Long_sword.png");
INSERT INTO weapon VALUES (DEFAULT, "Javelin", 1, 6, "Ranged", "Javelin.png");
INSERT INTO weapon VALUES (DEFAULT, "Longbow", 1, 8, "Ranged", "Long_bow.png");
INSERT INTO weapon VALUES (DEFAULT, "Short Sword", 1, 6, "Melee", "Short_sword.png");
INSERT INTO weapon VALUES (DEFAULT, "Dagger", 1, 4, "Melee", "Dagger.png");
INSERT INTO weapon VALUES (DEFAULT, "Hand Crossbow", 1, 6, "Ranged", "Crossbow.png");
INSERT INTO weapon VALUES (DEFAULT, "Chromatic Orb", 3, 8, "Ranged", "Chromatic_orb.png");
INSERT INTO weapon VALUES (DEFAULT, "Fireball", 6, 6, "Ranged","Fireball.png");

INSERT INTO weapon VALUES (DEFAULT, "Bite", 1, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Headbutt", 1, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Branch", 1, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Crow Ambush", 1, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Spook", 3, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Paper Swaddle", 3, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Great Bow", 3, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Dagger Throw", 3, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Poke", 1, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Cold Bite", 1, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Uppercut", 2, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Frost Spike", 2, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Horn Stab", 2, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Laser Vision", 2, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Tailwhip", 2, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Tongue Snipe", 2, 6, "Ranged", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Dragon claw", 2, 6, "Melee", NULL);
INSERT INTO weapon VALUES (DEFAULT, "Dragon fire", 6, 10, "Ranged", NULL);


--weapon and creature
INSERT INTO creature_weapon VALUES(1, 1);
INSERT INTO creature_weapon VALUES(2, 1);
INSERT INTO creature_weapon VALUES(3, 4);
INSERT INTO creature_weapon VALUES(4, 4);
INSERT INTO creature_weapon VALUES(5, 2);
INSERT INTO creature_weapon VALUES(6, 2);
INSERT INTO creature_weapon VALUES(7, 3);
INSERT INTO creature_weapon VALUES(8, 3);

INSERT INTO creature_weapon VALUES(9, 5);
INSERT INTO creature_weapon VALUES(10, 5);
INSERT INTO creature_weapon VALUES(11, 6);
INSERT INTO creature_weapon VALUES(12, 6);
INSERT INTO creature_weapon VALUES(13, 7);
INSERT INTO creature_weapon VALUES(14, 7);
INSERT INTO creature_weapon VALUES(15, 8);
INSERT INTO creature_weapon VALUES(16, 8);
INSERT INTO creature_weapon VALUES(17, 9);
INSERT INTO creature_weapon VALUES(18, 9);
INSERT INTO creature_weapon VALUES(19, 10);
INSERT INTO creature_weapon VALUES(20, 10);
INSERT INTO creature_weapon VALUES(21, 11);
INSERT INTO creature_weapon VALUES(22, 11);
INSERT INTO creature_weapon VALUES(23, 12);
INSERT INTO creature_weapon VALUES(24, 12);
INSERT INTO creature_weapon VALUES(25, 13);
INSERT INTO creature_weapon VALUES(26, 13);
INSERT INTO creature_weapon VALUES(25, 14);
INSERT INTO creature_weapon VALUES(26, 14);
INSERT INTO creature_weapon VALUES(25, 15);
INSERT INTO creature_weapon VALUES(26, 15);
INSERT INTO creature_weapon VALUES(25, 16);
INSERT INTO creature_weapon VALUES(26, 16);


