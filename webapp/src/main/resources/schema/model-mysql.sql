
CREATE TABLE audit (
    audit_id integer NOT NULL,
    event_type text,
    description text,
    time_recorded timestamp without time zone
);



CREATE TABLE collection (
    person_id integer NOT NULL,
    seq_id integer NOT NULL,
    name text,
    notes text,
    date_added timestamp without time zone
);

CREATE TABLE frag (
    frag_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    start_seq text,
    gc_content_percentage numeric,
    three character(1),
    four character(1),
    nineteen character(1),
    twenty character(1),
    three_plus_four integer,
    pots_nt text
);


CREATE TABLE global_collection (
    person_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    label character(10),
    notes text,
    date_added timestamp without time zone
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
);


CREATE TABLE global_history (
    global_history_id integer NOT NULL,
    global_seq_frag integer NOT NULL,
    person_id integer NOT NULL,
    params text,
    notes text,
    last_viewed timestamp without time zone
);



CREATE TABLE global_seq (
    global_seq_id integer NOT NULL,
    locus text,
    sequence text,
    gi_number integer,
    from_field text,
    name1 text,
    name2 text,
    description text,
    date_added timestamp without time zone
);

CREATE TABLE global_seq_frag (
    global_seq_frag integer NOT NULL,
    global_seq_id integer NOT NULL,
    frag_length integer NOT NULL,
    completed boolean,
    date_completed timestamp without time zone
);



CREATE TABLE "group" (
    group_id integer NOT NULL,
    name text,
    description text
);



CREATE TABLE group_global_seq (
    group_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    label text,
    notes text,
    date_added timestamp without time zone
);



CREATE TABLE group_seq (
    group_id integer NOT NULL,
    seq_id integer NOT NULL,
    name text,
    notes text,
    date_added timestamp without time zone
);



CREATE TABLE history (
    history_id integer NOT NULL,
    person_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    params text,
    notes text,
    last_viewed timestamp without time zone
);



CREATE TABLE person (
    person_id integer NOT NULL,
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
    creation_date timestamp with time zone
);

CREATE TABLE person_group (
    person_id integer NOT NULL,
    group_id integer NOT NULL,
    role text
);



CREATE TABLE queue (
    queue_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    complete boolean,
    date_completed timestamp without time zone,
    log_text text,
    priority_level integer NOT NULL
);


CREATE TABLE search_result (
    search_result_id integer NOT NULL,
    search_seq character varying(50),
    search_seq_rc text,
    result_seq text,
    result_accession text,
    result_accession_offset integer,
    result_offset integer,
    time_stamp timestamp without time zone
);



CREATE TABLE search_result_optimal (
    search_result_id integer NOT NULL,
    search_seq character varying(50),
    search_seq_rc text,
    result_seq text,
    result_accession text,
    result_accession_offset integer,
    result_offset integer,
    time_stamp timestamp without time zone
);


CREATE TABLE search_status (
    search_seq character varying(50) NOT NULL,
    search_seq_full text,
    status numeric(1,0) DEFAULT 0,
    entry_time timestamp without time zone,
    checkout_time timestamp without time zone,
    finish_time timestamp without time zone,
    read_count integer DEFAULT 0
);


CREATE TABLE seq (
    seq_id integer NOT NULL,
    person_id integer NOT NULL,
    sequence text,
    name text,
    description text,
    species text,
    date_added timestamp without time zone
);



CREATE TABLE seq_frag (
    seq_frag_id integer NOT NULL,
    frag_length integer NOT NULL,
    seq_id integer NOT NULL,
    completed boolean
);


CREATE TABLE seq_relationship (
    seq_id integer NOT NULL,
    global_seq_id integer NOT NULL,
    note text,
    type text
);


CREATE TABLE site_content (
    site_content_id integer NOT NULL,
    content text,
    label character varying(100),
    page text
);

CREATE TABLE submission (
    submission_id integer NOT NULL,
    person_id integer NOT NULL,
    seq_frag_id integer NOT NULL,
    seq_id integer NOT NULL,
    frag_length integer,
    date_requested timestamp without time zone
);



CREATE TABLE suffixtrie (
    id numeric NOT NULL,
    obj bytea
);

CREATE TABLE system_setting (
    system_setting character varying(20) NOT NULL,
    value text,
    enabled boolean
);



ALTER TABLE ONLY audit
    ADD CONSTRAINT audit_pkey PRIMARY KEY (audit_id);



ALTER TABLE ONLY collection
    ADD CONSTRAINT collection_pkey PRIMARY KEY (person_id, seq_id);



ALTER TABLE ONLY frag
    ADD CONSTRAINT frag_pkey PRIMARY KEY (frag_id);


ALTER TABLE ONLY global_collection
    ADD CONSTRAINT global_collection_pkey PRIMARY KEY (person_id);



ALTER TABLE ONLY global_frag
    ADD CONSTRAINT global_frag_pkey PRIMARY KEY (global_frag_id);


ALTER TABLE ONLY global_history
    ADD CONSTRAINT global_history_pkey PRIMARY KEY (global_history_id);



ALTER TABLE ONLY global_seq_frag
    ADD CONSTRAINT global_seq_frag_pkey PRIMARY KEY (global_seq_frag);



ALTER TABLE ONLY global_seq
    ADD CONSTRAINT global_seq_pkey PRIMARY KEY (global_seq_id);



ALTER TABLE ONLY group_global_seq
    ADD CONSTRAINT group_global_seq_pkey PRIMARY KEY (group_id, global_seq_id);



ALTER TABLE ONLY "group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (group_id);



ALTER TABLE ONLY group_seq
    ADD CONSTRAINT group_seq_pkey PRIMARY KEY (group_id, seq_id);



ALTER TABLE ONLY history
    ADD CONSTRAINT history_pkey PRIMARY KEY (history_id);



ALTER TABLE ONLY person_group
    ADD CONSTRAINT person_group_pkey PRIMARY KEY (person_id, group_id);



ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (person_id);



ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (queue_id);



ALTER TABLE ONLY search_result_optimal
    ADD CONSTRAINT search_result_optimal_pkey PRIMARY KEY (search_result_id);



ALTER TABLE ONLY search_result
    ADD CONSTRAINT search_result_pkey PRIMARY KEY (search_result_id);



ALTER TABLE ONLY search_status
    ADD CONSTRAINT search_status_pkey PRIMARY KEY (search_seq);



ALTER TABLE ONLY seq_frag
    ADD CONSTRAINT seq_frag_pkey PRIMARY KEY (seq_frag_id);



ALTER TABLE ONLY seq
    ADD CONSTRAINT seq_pkey PRIMARY KEY (seq_id);



ALTER TABLE ONLY seq_relationship
    ADD CONSTRAINT seq_relationship_pkey PRIMARY KEY (seq_id, global_seq_id);



ALTER TABLE ONLY site_content
    ADD CONSTRAINT site_id PRIMARY KEY (site_content_id);



ALTER TABLE ONLY site_content
    ADD CONSTRAINT siteuniq UNIQUE (label);



ALTER TABLE ONLY submission
    ADD CONSTRAINT submission_pkey PRIMARY KEY (submission_id);



ALTER TABLE ONLY suffixtrie
    ADD CONSTRAINT suffixtrie_pk PRIMARY KEY (id);

--

ALTER TABLE ONLY system_setting
    ADD CONSTRAINT system_setting_pkey PRIMARY KEY (system_setting);



CREATE INDEX idx_status ON search_status USING btree (search_seq, status);



ALTER TABLE ONLY global_history
    ADD CONSTRAINT fk_active_seqs_1 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY frag
    ADD CONSTRAINT fk_frag_1 FOREIGN KEY (seq_frag_id) REFERENCES seq_frag(seq_frag_id);



ALTER TABLE ONLY global_collection
    ADD CONSTRAINT fk_global_collection_2 FOREIGN KEY (global_seq_id) REFERENCES global_seq(global_seq_id);



ALTER TABLE ONLY global_frag
    ADD CONSTRAINT fk_global_frag_1 FOREIGN KEY (global_seq_frag) REFERENCES global_seq_frag(global_seq_frag);



ALTER TABLE ONLY global_history
    ADD CONSTRAINT fk_global_history_2 FOREIGN KEY (global_seq_frag) REFERENCES global_seq_frag(global_seq_frag);



ALTER TABLE ONLY global_seq_frag
    ADD CONSTRAINT fk_global_seq_frag_1 FOREIGN KEY (global_seq_id) REFERENCES global_seq(global_seq_id);



ALTER TABLE ONLY group_global_seq
    ADD CONSTRAINT fk_group_global_seq_1 FOREIGN KEY (group_id) REFERENCES "group"(group_id);


--
ALTER TABLE ONLY group_global_seq
    ADD CONSTRAINT fk_group_global_seq_2 FOREIGN KEY (global_seq_id) REFERENCES global_seq(global_seq_id);



ALTER TABLE ONLY group_seq
    ADD CONSTRAINT fk_group_seq_1 FOREIGN KEY (group_id) REFERENCES "group"(group_id);



ALTER TABLE ONLY group_seq
    ADD CONSTRAINT fk_group_seq_2 FOREIGN KEY (seq_id) REFERENCES seq(seq_id);



ALTER TABLE ONLY history
    ADD CONSTRAINT fk_history_1 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY history
    ADD CONSTRAINT fk_history_2 FOREIGN KEY (seq_frag_id) REFERENCES seq_frag(seq_frag_id);



ALTER TABLE ONLY person_group
    ADD CONSTRAINT fk_person_group_1 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY person_group
    ADD CONSTRAINT fk_person_group_2 FOREIGN KEY (group_id) REFERENCES "group"(group_id);



ALTER TABLE ONLY queue
    ADD CONSTRAINT fk_queue_1 FOREIGN KEY (seq_frag_id) REFERENCES seq_frag(seq_frag_id);



ALTER TABLE ONLY search_result
    ADD CONSTRAINT fk_search_result_1 FOREIGN KEY (search_seq) REFERENCES search_status(search_seq);



ALTER TABLE ONLY seq
    ADD CONSTRAINT fk_seq_1 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY seq_frag
    ADD CONSTRAINT fk_seq_frag_1 FOREIGN KEY (seq_id) REFERENCES seq(seq_id);



ALTER TABLE ONLY seq_relationship
    ADD CONSTRAINT fk_seq_relationship_1 FOREIGN KEY (seq_id) REFERENCES seq(seq_id);



ALTER TABLE ONLY seq_relationship
    ADD CONSTRAINT fk_seq_relationship_2 FOREIGN KEY (global_seq_id) REFERENCES global_seq(global_seq_id);



ALTER TABLE ONLY submission
    ADD CONSTRAINT fk_submission_2 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY submission
    ADD CONSTRAINT fk_submission_3 FOREIGN KEY (seq_frag_id) REFERENCES seq_frag(seq_frag_id);



ALTER TABLE ONLY submission
    ADD CONSTRAINT fk_submission_4 FOREIGN KEY (seq_id) REFERENCES seq(seq_id);



ALTER TABLE ONLY global_collection
    ADD CONSTRAINT fk_table_16_2 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY collection
    ADD CONSTRAINT fk_table_17_1 FOREIGN KEY (person_id) REFERENCES person(person_id);



ALTER TABLE ONLY collection
    ADD CONSTRAINT fk_table_17_2 FOREIGN KEY (seq_id) REFERENCES seq(seq_id);


