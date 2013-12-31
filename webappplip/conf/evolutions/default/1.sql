# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table PlipUser (
  id_user                   integer auto_increment not null,
  name                      varchar(255) not null,
  last_name                 varchar(255) not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_PlipUser primary key (id_user))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table PlipUser;

SET FOREIGN_KEY_CHECKS=1;

