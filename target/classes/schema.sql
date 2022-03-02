CREATE SCHEMA IF NOT EXISTS products;

drop table if exists Products;
drop table if exists Tags;

--Use Products;--

create table IF NOT EXISTS Products
(
	id		varchar(255)	not null unique,
	name	varchar(255)	not null,
	description	varchar(255)	not null,
	brand	varchar(255)	not null,
	category	varchar(255)	not null,
	created_at	varchar(255)	not null,
	PRIMARY KEY (id)
);

create table IF NOT EXISTS Tags
(
	tid int	not null auto_increment,
	pid varchar(255)	not null,
	tag varchar(255)	not null,
	PRIMARY KEY (tid,pid), FOREIGN KEY (pid) REFERENCES Products(id)
);
