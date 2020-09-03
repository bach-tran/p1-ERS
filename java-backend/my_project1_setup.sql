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
	password VARCHAR(50) NOT NULL,
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
VALUES ('bach_tran', '12345', 'Bach', 'Tran', 'bach_tran@outlook.com', 1);

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

