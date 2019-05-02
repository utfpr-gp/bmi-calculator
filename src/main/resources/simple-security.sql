DROP DATABASE IF EXISTS bmi_calculator_db;
CREATE DATABASE bmi_calculator_db;
USE bmi_calculator_db;

CREATE TABLE users (
	login varchar(20) NOT NULL PRIMARY KEY,
	pwd varchar(64) NOT NULL
);
CREATE TABLE roles (
	login varchar(20) NOT NULL,
  role varchar(20) NOT NULL,
	primary key(login, role)
);

insert into users values ('root', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5');
insert into roles values ('root', 'admin');
insert into roles values ('root', 'user');
insert into users values ('roni', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5');
insert into roles values ('roni', 'admin');
insert into users values ('fabio', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5');
insert into roles values ('fabio', 'user');
COMMIT;

