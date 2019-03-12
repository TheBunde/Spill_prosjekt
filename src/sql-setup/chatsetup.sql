CREATE TABLE chat(
    chat_id INTEGER NOT NULL AUTO_INCREMENT,
    CONSTRAINT chat_pk PRIMARY KEY(chat_id));

CREATE TABLE chatter(
    chatter_id INTEGER NOT NULL AUTO_INCREMENT,
    chat_id INTEGER,
    name VARCHAR(30) NOT NULL,
    CONSTRAINT chatter_pk PRIMARY KEY(chatter_id));

CREATE TABLE chat_message(
    chat_id INTEGER NOT NULL,
    message_id INTEGER NOT NULL AUTO_INCREMENT,
    chatter_id INTEGER NOT NULL,
    message VARCHAR(30) NOT NULL,
    time_stamp TIME NOT NULL,
    CONSTRAINT chat_message_pk1 PRIMARY KEY(message_id, chat_id));

ALTER TABLE chatter
  ADD CONSTRAINT chatter_fk FOREIGN KEY(chat_id) REFERENCES chat(chat_id);
  
ALTER TABLE chat_message
  ADD CONSTRAINT chat_message_fk1 FOREIGN KEY(chat_id) REFERENCES chat(chat_id);
  
ALTER TABLE chat_message
  ADD CONSTRAINT chat_message_fk2 FOREIGN KEY(chatter_id) REFERENCES chatter(chatter_id);
  
  
INSERT INTO chat VALUES(1);
INSERT INTO chatter VALUES(DEFAULT, 1, 'William');