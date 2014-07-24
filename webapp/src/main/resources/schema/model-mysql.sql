CREATE TABLE schemaname.person (
       person_id INT NOT NULL
     , first_name TEXT
     , last_name TEXT
     , dob DATE
     , PRIMARY KEY (person_id)
);

CREATE TABLE schemaname.studio (
       studio_id INT NOT NULL
     , name TEXT
     , website TEXT
     , PRIMARY KEY (studio_id)
);

CREATE TABLE schemaname.movie (
       movie_id INT NOT NULL
     , title TEXT
     , year INT
     , description TEXT
     , studio_id INT NOT NULL
     , PRIMARY KEY (movie_id)
     , CONSTRAINT FK_movie_1 FOREIGN KEY (studio_id)
                  REFERENCES schemaname.studio (studio_id)
);

CREATE TABLE schemaname.actors (
       person_id INT NOT NULL
     , movie_id INT NOT NULL
     , PRIMARY KEY (person_id, movie_id)
     , CONSTRAINT FK_actors_1 FOREIGN KEY (person_id)
                  REFERENCES schemaname.person (person_id)
     , CONSTRAINT FK_actors_2 FOREIGN KEY (movie_id)
                  REFERENCES schemaname.movie (movie_id)
);

CREATE TABLE schemaname.showing (
       theater VARCHAR(50) NOT NULL
     , day VARCHAR(50) NOT NULL
     , movie_id INT NOT NULL
     , PRIMARY KEY (theater, day, movie_id)
     , CONSTRAINT FK_showing_1 FOREIGN KEY (movie_id)
                  REFERENCES schemaname.movie (movie_id)
);

