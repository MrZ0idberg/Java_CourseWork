INSERT INTO post(name_post)
VALUES ("Завідувач"),("Бібліотекар")
;
INSERT INTO workers(login, password, post_id)
VALUES ("admin", "admin", 1)
;
INSERT INTO library_reader(login, password)
VALUES ("user", "user")
;

INSERT INTO book_genre(name_genre)
VALUES ("Антиутопія"), ("Детектив")
;
INSERT INTO books_department(name_department)
VALUES ("Головний відділ"), ("Лівий відділ")
;
INSERT INTO books(name_book, writer, genre_id, department_id, isAvailable)
VALUES ("Лялька", "Даніель Коул", "2", "1","1"),
("Кат", "Даніель Коул", "2", "2","0"),
("Колгосп тварин", "Джордж Орвелл", "1", "1","1"),
("Прекрасний новий світ", "Олдос Гакслі", "1", "2","0") 
;

INSERT INTO accounting_books(books_id, library_reader_id, accounting_books.return)
VALUES ("1", "1", "1"), 
("1", "1", "1"), 
("2", "1", "1"), 
("2", "1", "0"), 
("3", "1", "1"), 
("3", "1", "0"),
("4", "1", "1"), 
("4", "1", "0")  
;
INSERT INTO books(name_book, writer, genre_id, department_id, isAvailable)
VALUES ("Ліва книга", "Лівий автор", "2", "1", "1")
;