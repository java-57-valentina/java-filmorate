DELETE FROM genres;
DELETE FROM ratings;
-- DELETE FROM users;

INSERT INTO genres (name) VALUES ('Action');
INSERT INTO genres (name) VALUES ('Comedy');
INSERT INTO genres (name) VALUES ('Drama');
INSERT INTO genres (name) VALUES ('Fantasy');
INSERT INTO genres (name) VALUES ('Horror');
INSERT INTO genres (name) VALUES ('Mystery');
INSERT INTO genres (name) VALUES ('Romance');
INSERT INTO genres (name) VALUES ('Thriller');
INSERT INTO genres (name) VALUES ('Western');

INSERT INTO ratings (name) VALUES ('G');
INSERT INTO ratings (name) VALUES ('PG');
INSERT INTO ratings (name) VALUES ('PG-13');
INSERT INTO ratings (name) VALUES ('R');
INSERT INTO ratings (name) VALUES ('NC-17');