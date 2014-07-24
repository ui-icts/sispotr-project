
CREATE TABLE audit (
    audit_id integer NOT NULL AUTO_INCREMENT ,
    event_type text,
    description text,
    time_recorded timestamp ,
    primary key(audit_id)
);



CREATE TABLE collection (
    person_id integer NOT NULL ,
    seq_id integer NOT NULL,
    name text,
    notes text,
    date_added timestamp 
);

CREATE TABLE frag (
    frag_id integer NOT NULL AUTO_INCREMENT,
    seq_frag_id integer NOT NULL,
    start_seq text,
    gc_content_percentage numeric,
    three character(1),
    four character(1),
    nineteen character(1),
    twenty character(1),
    three_plus_four integer,
    pots_nt text,
    primary key(frag_id)

);


CREATE TABLE global_collection (
    person_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    label character(10),
    notes text,
    date_added timestamp 
);


CREATE TABLE global_frag (
    global_frag_id integer NOT NULL,
    start_seq text,
    gc_content_percentage numeric,
    three character(1),
    four character(1),
    nineteen character(1),
    twenty character(1),
    three_plus_four integer,
    pots_nt text,
    global_seq_frag integer NOT NULL
    , primary key(global_frag_id)
);


CREATE TABLE global_history (
    global_history_id integer NOT NULL AUTO_INCREMENT,
    global_seq_frag integer NOT NULL,
    person_id integer NOT NULL,
    params text,
    notes text,
    last_viewed timestamp 
    , primary key(global_history_id)
);



CREATE TABLE global_seq (
    global_seq_id integer NOT NULL ,
    locus text,
    sequence text,
    gi_number integer,
    from_field text,
    name1 text,
    name2 text,
    description text,
    date_added timestamp 
);

CREATE TABLE global_seq_frag (
    global_seq_frag integer NOT NULL,
    global_seq_id integer NOT NULL,
    frag_length integer NOT NULL,
    completed boolean,
    date_completed timestamp 
);



CREATE TABLE `group` (
    group_id integer NOT NULL AUTO_INCREMENT,
    name text,
    description text
 , primary key(group_id)
);



CREATE TABLE group_global_seq (
    group_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    label text,
    notes text,
    date_added timestamp 
);



CREATE TABLE group_seq (
    group_id integer NOT NULL,
    seq_id integer NOT NULL ,
    name text,
    notes text,
    date_added timestamp 
);



CREATE TABLE history (
    history_id integer NOT NULL AUTO_INCREMENT,
    person_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    params text,
    notes text,
    last_viewed timestamp 
     , primary key(history_id)
);



CREATE TABLE person (
    person_id integer NOT NULL AUTO_INCREMENT,
    username text,
    guid text,
    domain text,
    email text,
    organization text,
    password text,
    access_level integer DEFAULT 0,
    verification_key text,
    verified boolean,
    industry text,
    creation_date timestamp 
, primary key(person_id)
);

CREATE TABLE person_group (
    person_id integer NOT NULL,
    group_id integer NOT NULL,
    role text
);



CREATE TABLE queue (
    queue_id integer NOT NULL AUTO_INCREMENT,
    seq_frag_id integer NOT NULL,
    complete boolean,
    date_completed timestamp ,
    log_text text,
    priority_level integer NOT NULL
	, primary key(queue_id)
);


CREATE TABLE search_result (
    search_result_id integer NOT NULL AUTO_INCREMENT,
    search_seq character varying(50),
    search_seq_rc text,
    result_seq text,
    result_accession text,
    result_accession_offset integer,
    result_offset integer,
    time_stamp timestamp 
    , primary key(search_result_id)
);



CREATE TABLE search_result_optimal (
    search_result_id integer NOT NULL AUTO_INCREMENT,
    search_seq character varying(50),
    search_seq_rc text,
    result_seq text,
    result_accession text,
    result_accession_offset integer,
    result_offset integer,
    time_stamp timestamp 
 , primary key(search_result_id)
);


CREATE TABLE search_status (
    search_seq character varying(50) NOT NULL,
    search_seq_full text,
    status numeric(1,0) DEFAULT 0,
    entry_time timestamp ,
    checkout_time timestamp ,
    finish_time timestamp ,
    read_count integer DEFAULT 0
);


CREATE TABLE seq (
    seq_id integer NOT NULL AUTO_INCREMENT,
    person_id integer NOT NULL,
    sequence text,
    name text,
    description text,
    species text,
    date_added timestamp 
, primary key(seq_id)
);



CREATE TABLE seq_frag (
    seq_frag_id integer NOT NULL AUTO_INCREMENT,
    frag_length integer NOT NULL,
    seq_id integer NOT NULL,
    completed boolean
, primary key(seq_frag_id)
);


CREATE TABLE seq_relationship (
    seq_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    note text,
    type text
);


CREATE TABLE site_content (
    site_content_id integer NOT NULL AUTO_INCREMENT,
    content text,
    label character varying(100),
    page text
, primary key(site_content_id)
);

CREATE TABLE submission (
    submission_id integer NOT NULL AUTO_INCREMENT,
    person_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    seq_id integer NOT NULL,
    frag_length integer,
    date_requested timestamp 
, primary key(submission_id)
);



CREATE TABLE suffixtrie (
    id integer NOT NULL AUTO_INCREMENT,
    obj blob,
primary key(id)
);

CREATE TABLE system_setting (
    system_setting character varying(20) NOT NULL,
    value text,
    enabled boolean
);


ALTER TABLE site_content
    ADD CONSTRAINT siteuniq UNIQUE (label);


CREATE INDEX idx_status ON search_status  (search_seq, status) USING btree;

