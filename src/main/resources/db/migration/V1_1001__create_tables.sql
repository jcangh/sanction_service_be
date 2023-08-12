create table sanction_institution (
  id                             varchar(36),  -- uuid
  -- -------------------------------------------------
  name          	 			 varchar(50),
  type           	 	         varchar(25),
  -- -------------------------------------------------
  created_on                 	 timestamp,
  updated_on                 	 timestamp,
  created_by                     varchar(50),
  updated_by                     varchar(50),
  primary key (id)
);

create table sanction (
  id                             varchar(36),  -- uuid
  -- -------------------------------------------------
  name          	 			 varchar(50),
  type           	 	         varchar(25),
  -- -------------------------------------------------
  origin_id                      varchar(36),
  destination_id                 varchar(36),
  state                          varchar(25),
  additional_values              json,
  -- -------------------------------------------------
  created_on                 	 timestamp,
  updated_on                 	 timestamp,
  created_by                     varchar(50),
  updated_by                     varchar(50),
  primary key (id)
);

create table sanction_action (
  id                             varchar(36),  -- uuid
  -- -------------------------------------------------
  previous_state                 varchar(25),
  new_state                      varchar(25),
  event                          varchar(25),
  note                           varchar(100),
  approved_by                    varchar(50),
  sanction_id                    varchar(36),
  -- -------------------------------------------------
  created_on                 	 timestamp,
  updated_on                 	 timestamp,
  created_by                     varchar(50),
  updated_by                     varchar(50),
  primary key (id)
);

alter table sanction add foreign key (origin_id) references sanction_institution(id);
alter table sanction add foreign key (destination_id) references sanction_institution(id);
alter table sanction_action add foreign key (sanction_id) references sanction(id);