create database driveds;
use driveds;

create table USUARIO 
(
	USU_ID          	   integer NOT NULL AUTO_INCREMENT,
    USU_LOGIN       	   varchar(100) not null,
    USU_SENHA       	   varchar(100) not null,
    USU_EMAIL       	   varchar(100) not null,
	USU_DIRE_ARQUIVO       varchar(100) not null,
    USU_DH_CADASTRO timestamp not null,
    primary key(USU_ID)
) ENGINE = INNODB;

create table COMPARTILHAMENTO (

COMP_ID     integer NOT NULL AUTO_INCREMENT,
USU_ID_PROP integer NOT NULL,
FILE_NAME   VARCHAR(500) NOT NULL,
USU_ID_COMP integer NOT NULL,
COMP_SYN    Bool NOT NULL DEFAULT 0,

primary key (COMP_ID)

) ENGINE=innoDB;

create table ARQUIVO (

ARQ_ID      integer NOT NULL AUTO_INCREMENT,
USU_ID integer NOT NULL,
ARQ_NM   VARCHAR(500) NOT NULL,
ARQ_SYN    Bool NOT NULL DEFAULT 0,
ARQ_REMOVIDO Bool NOT NULL DEFAULT 0,

primary key (ARQ_ID)

) ENGINE=innoDB;

alter table driveds.COMPARTILHAMENTO add constraint fk_id_user_dono_file1 foreign key (USU_ID_PROP) references driveds.USUARIO (USU_ID);
alter table driveds.COMPARTILHAMENTO add constraint fk_id_user_comp_file1 foreign key (USU_ID_COMP) references driveds.USUARIO (USU_ID);
alter table driveds.ARQUIVO add constraint fk_id_user_arquivo foreign key (USU_ID) references driveds.USUARIO (USU_ID);

commit;