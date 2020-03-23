CREATE TABLE writingGroup ( /* Table for Writing Group*/
   gName        VARCHAR(100) NOT NULL, /*Column for group name*/
   headWriter   VARCHAR(100) NOT NULL, /*Column for group's head writer*/
   yearFounded  INT          NOT NULL, /*Column for year group was foubded*/
   subject      VARCHAR(100) NOT NULL, /*Column for group's subject*/
   CONSTRAINT writingGroup_pk PRIMARY KEY (gName)); /*Group name is primary key*/

CREATE TABLE Publisher ( /* Table for Publisher*/
   pName      VARCHAR(100) NOT NULL, /*Column for publisher name*/
   pAddress   VARCHAR(100) NOT NULL, /*Column for publisher address*/
   pPhone     VARCHAR(100) NOT NULL, /*Column for publisher phone number*/
   pEmail     VARCHAR(100) NOT NULL, /*Column for publisher email address*/
CONSTRAINT publisher_pk  PRIMARY KEY (pName)); /*Publisher name is foreign key*/

CREATE TABLE Book ( /* Table for Book*/
   gName           VARCHAR(100) NOT NULL, /*Column for book's group name*/
   bTitle          VARCHAR(100) NOT NULL, /*Column for book title*/
   pName           VARCHAR(100) NOT NULL, /*Column for book's publisher name*/
   yearPublished   INT          NOT NULL, /*Column for year book was published*/
   numberPages     INT          NOT NULL, /*Column for number of pages in book*/
CONSTRAINT books_pk  PRIMARY KEY (gName, bTitle), /*Group name and book title are primary keys*/
CONSTRAINT book_fk   FOREIGN KEY(gName) REFERENCES writingGroup(gName), /*Group name is foreign key*/
CONSTRAINT book_fk_2 FOREIGN KEY (pName) REFERENCES Publisher(pName),
CONSTRAINT book_uk1  UNIQUE (bTitle)); /*Publisher name has a uniqueness constraint as a candidate key*/

/*insert statements*/
INSERT INTO writingGroup (gName, headWriter, yearFounded, subject) VALUES
    ('Database Group', 'David Brown', 2020, 'SQL'),
    ('Information Systems Group', 'Robert McClean', 2015, 'System Design'),
    ('Algorithms Group', 'Darin Goldstein', 2010, 'NP Hard Problems'),
    ('Graphic Arts Group', 'Tor Hovind', 2012, 'User Interface'),
    ('Cyber Security Group', 'Louis Uuh', 2017,'Encryption'),
    ('Health Group', 'Gail Farmer', 2000, 'Nursing');

INSERT INTO Publisher (pName, pAddress, pPhone, pEmail) VALUES
    ('Computer Science Publishers', '10101 Binary Blvd', '101-010-1010', 'cspublishers@binary.com'),
    ('Buisness Publishing House', '500 Wall St', '987-654-3210', 'bsh@ws.com'),
    ('Art Press', '4114 Mural Way', '908-123-4567', 'art@press.com'),
    ('Health Pub', '911 Help Ln', '911-911-9119', 'help@health.com');

INSERT INTO Book (gName, bTitle, pName, yearPublished, numberPages) VALUES
    ('Database Group', 'Database Fundamentals in SQL', 'Computer Science Publishers', 2020, 400),
    ('Information Systems Group', 'Early Concepts of System Design', 'Buisness Publishing House', 2017, 100),
    ('Algorithms Group', 'Solving NP Hard Problems', 'Computer Science Publishers', 2018, 600),
    ('Graphic Arts Group', 'Designing for the User', 'Art Press', 2013, 150),
    ('Cyber Security Group', 'RSA', 'Computer Science Publishers', 2018, 1000),
    ('Health Group', 'Coronavirus', 'Health Pub', 2020, 1020);
