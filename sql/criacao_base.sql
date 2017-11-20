create database driveds;
use driveds;

create table usuario 
(
	USU_ID          	   integer NOT NULL AUTO_INCREMENT,
    USU_LOGIN       	   varchar(100) not null,
    USU_SENHA       	   varchar(100) not null,
    USU_EMAIL       	   varchar(100) not null,
	USU_DIRE_ARQUIVO       varchar(100) not null,
    USU_DH_CADASTRO timestamp not null,
    primary key(USU_ID)
) ENGINE = INNODB;

create table compartilhamento (

COMP_ID     integer NOT NULL AUTO_INCREMENT,
USU_ID_PROP integer NOT NULL,
FILE_NAME   VARCHAR(500) NOT NULL,
USU_ID_COMP integer NOT NULL,
COMP_SYN    Bool NOT NULL DEFAULT 0,

primary key (COMP_ID)

) ENGINE=innoDB;

alter table driveds.compartilhamento add constraint fk_id_user_dono_file1 foreign key (USU_ID_PROP) references driveds.usuario (USU_ID);
alter table driveds.compartilhamento add constraint fk_id_user_comp_file1 foreign key (USU_ID_COMP) references driveds.usuario (USU_ID);

commit;