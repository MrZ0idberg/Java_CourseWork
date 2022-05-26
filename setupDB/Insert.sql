INSERT INTO post(name_post)
VALUES ("Завідувач"),("Бібліотекар")
;

INSERT INTO workers(login, password, post_id, name, last_name, phone_num)
VALUES ("admin", "d033e22ae348aeb5660fc2140aec35850c4da997", 1, "Любомир", "Шевченко", "0661711971"),
("worker1", "3c05842098ae1ee828d393f2d1b2a17d7270bffc", 2, "Андрюха", "Фурман", "0661232545")
;

INSERT INTO library_reader(login, password, name, last_name, phone_num)
VALUES ('serega', '5eb463ee224b865c5b4eb36ae68b3081bc33c398', 'Сергій', 'Антропов', '0661712971'),
('revo', 'e662c46b5bef24a96c3128e25f43beaa05e3bd13', 'Володимир', 'Ревуцький', '0661732971')
;

INSERT INTO book_genre(name_genre)
VALUES ("Антиутопія"), ("Детектив"), ("Книжки для мазохістів")
;

INSERT INTO books_department(name_department)
VALUES ("Головний відділ"), ("Лівий відділ")
;

INSERT INTO books(name_book, writer, genre_id, department_id, isAvailable)
VALUES ("Лялька", "Даніель Коул", "2", "1","1"),
("Кат", "Даніель Коул", "2", "2","1"),
("Колгосп тварин", "Джордж Орвелл", "1", "1","1"),
("Прекрасний новий світ", "Олдос Гакслі", "1", "2","1"),
("Кат", "Даніель Коул", "2", "1","1"),
("Правда про справу Гаррі Квеберта", "Жоель Діккер", "2", "1","1"),
("Комп'ютерні мережі: принципи, технології, протоколи", "Віктор Оліфер", "3", "1","1")
;
