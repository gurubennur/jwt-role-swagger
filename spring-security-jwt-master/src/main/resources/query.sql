drop table if exists role
drop table if exists user
drop table if exists user_roles
create table role (id bigint not null auto_increment, description varchar(255), name varchar(255), primary key (id)) engine=MyISAM
create table user (id bigint not null auto_increment, phone integer, password varchar(255), email varchar(25), username varchar(255), primary key (id)) engine=MyISAM
create table user_roles (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id)) engine=MyISAM
alter table user_roles add constraint FKrhfovtciq1l558cw6udg0h0d3 foreign key (role_id) references role (id)
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id)

INSERT INTO user (id, username, password, email, phone) VALUES (1, 'user1', '$2a$10$A2ySJinwRJzHMG4OgSRoDOJLw2xcVfY/yLTD3BZwXBO2eD08mTgUy', 'Guru@gmail.com', 8073600389);

INSERT INTO role (id, description, name) VALUES (1, 'Admin role', 'ADMIN');
INSERT INTO role (id, description, name) VALUES (2, 'User role', 'USER');

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);