DROP TABLE IF EXISTS user_roles CASCADE;
CREATE TABLE user_roles (
	id INTEGER PRIMARY KEY,
	role VARCHAR(10) NOT NULL
);

INSERT INTO user_roles (id, role) VALUES 
(1, 'EMPLOYEE'), 
(2, 'MANAGER');

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(64) NOT NULL,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	email VARCHAR(150) NOT NULL,
	role_id INTEGER NOT NULL,
	CONSTRAINT fk_users_roles
		FOREIGN KEY (role_id)
		REFERENCES user_roles (id),
	CONSTRAINT users_unique
		UNIQUE(username, email)
);

INSERT INTO users (username, password, first_name, last_name, email, role_id)
VALUES ('bach_tran', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', 'Bach', 'Tran', 'bach_tran@outlook.com', 1),
('john_doe', 'b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32', 'John', 'Doe', 'john_doe@outlook.com', 2),
('jane_doe', 'cf80cd8aed482d5d1527d7dc72fceff84e6326592848447d2dc0b0e87dfc9a90', 'Jane', 'Doe', 'jane_doe@outlook.com', 1),
('bob_rodriguez', '6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090', 'Bob', 'Rodriguez', 'bob_rodriguez@outlook.com', 2);

DROP TABLE IF EXISTS r_status CASCADE;
CREATE TABLE r_status (
	id INTEGER PRIMARY KEY,
	status VARCHAR(10) NOT NULL
);

INSERT INTO r_status (id, status) VALUES 
(1, 'PENDING'), 
(2, 'APPROVED'), 
(3, 'DENIED');

DROP TABLE IF EXISTS r_type CASCADE;
CREATE TABLE r_type (
	id INTEGER PRIMARY KEY,
	type VARCHAR(10) NOT NULL
);

INSERT INTO r_type (id, type) VALUES 
(1, 'LODGING'), 
(2, 'TRAVEL'), 
(3, 'FOOD'), 
(4, 'OTHER');

DROP TABLE IF EXISTS reimbursements CASCADE;
CREATE TABLE reimbursements (
	id SERIAL PRIMARY KEY,
	amount DOUBLE PRECISION NOT NULL,
	submitted TIMESTAMP NOT NULL,
	resolved TIMESTAMP,
	description VARCHAR(250),
	receipt bytea,
	author INTEGER NOT NULL,
	resolver INTEGER,
	status_id INTEGER NOT NULL,
	type_id INTEGER NOT NULL,
	CONSTRAINT fk_reimbursements_users1
		FOREIGN KEY (author)
		REFERENCES users (id),
	CONSTRAINT fk_reimbursements_users2
		FOREIGN KEY (resolver)
		REFERENCES users (id),
	CONSTRAINT fk_reimbursements_status
		FOREIGN KEY (status_id)
		REFERENCES r_status (id),
	CONSTRAINT fk_reimbursements_type
		FOREIGN KEY (type_id)
		REFERENCES r_type (id)
);

