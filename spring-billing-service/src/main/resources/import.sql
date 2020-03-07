/* Populate tables */
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Franco', 'Arratia', 'franco@extreme.com', '2017-08-28', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Doe', 'john.doe@gmail.com', '2017-08-28', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Murillo', 'john.doe@gmail.com', '2017-08-02', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2017-08-03', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Jane', 'Doe', 'jane.doe@gmail.com', '2017-08-04', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2017-08-05', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Erich', 'Gamma', 'erich.gamma@gmail.com', '2017-08-06', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Richard', 'Helm', 'richard.helm@gmail.com', '2017-08-07', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2017-08-08', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Vlissides', 'john.vlissides@gmail.com', '2017-08-09', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('James', 'Gosling', 'james.gosling@gmail.com', '2017-08-010', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Bruce', 'Lee', 'bruce.lee@gmail.com', '2017-08-11', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Johnny', 'Doe', 'johnny.doe@gmail.com', '2017-08-12', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Roe', 'john.roe@gmail.com', '2017-08-13', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Jane', 'Roe', 'jane.roe@gmail.com', '2017-08-14', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Richard', 'Doe', 'richard.doe@gmail.com', '2017-08-15', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Janie', 'Doe', 'janie.doe@gmail.com', '2017-08-16', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Phillip', 'Webb', 'phillip.webb@gmail.com', '2017-08-17', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Stephane', 'Nicoll', 'stephane.nicoll@gmail.com', '2017-08-18', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Sam', 'Brannen', 'sam.brannen@gmail.com', '2017-08-19', '');  
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Juergen', 'Hoeller', 'juergen.Hoeller@gmail.com', '2017-08-20', ''); 
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Janie', 'Roe', 'janie.roe@gmail.com', '2017-08-21', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Smith', 'john.smith@gmail.com', '2017-08-22', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Joe', 'Bloggs', 'joe.bloggs@gmail.com', '2017-08-23', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('John', 'Stiles', 'john.stiles@gmail.com', '2017-08-24', '');
INSERT INTO clients (name, last_name, email, created_at, photo) VALUES('Richard', 'Roe', 'stiles.roe@gmail.com', '2017-08-25', '');

/* Populate tabla productos */
INSERT INTO products (name, price, created_at) VALUES('Panasonic Pantalla LCD', 250, NOW());
INSERT INTO products (name, price, created_at) VALUES('Sony Camara digital DSC-W320B', 120, NOW());
INSERT INTO products (name, price, created_at) VALUES('Apple iPod shuffle', 140, NOW());
INSERT INTO products (name, price, created_at) VALUES('Sony Notebook Z110', 370, NOW());
INSERT INTO products (name, price, created_at) VALUES('Hewlett Packard Multifuncional F2280', 690, NOW());
INSERT INTO products (name, price, created_at) VALUES('Bianchi Bicicleta Aro 26', 690, NOW());
INSERT INTO products (name, price, created_at) VALUES('Mica Comoda 5 Cajones', 290, NOW());

/* Creamos algunos usuarios con sus roles */
INSERT INTO users (username, password, enabled) VALUES ('franco','$2a$10$MMUeXTxSqhf4f7sdCmlrv.shB2VYTtKGLDNo0LJx6yxrPlSgJNbPy',1);
INSERT INTO users (username, password, enabled) VALUES ('admin','$2a$10$gPCkOt2LzmM7EpYUN8AMHuQLIh0wZSNyy5jRMVcXz61.gvRaPIGUW',1);

INSERT INTO authorities (user_id, authority) VALUES (1,'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_ADMIN');
INSERT INTO authorities (user_id, authority) VALUES (2,'ROLE_USER');