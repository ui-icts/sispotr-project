CREATE SEQUENCE safeseed.seqnum
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 31
  CACHE 1;
CREATE TABLE safeseed.system_setting (
       system_setting VARCHAR(20) NOT NULL
     , value TEXT
     , enabled BOOLEAN
     , PRIMARY KEY (system_setting)
);

CREATE TABLE safeseed.audit (
       audit_id INT NOT NULL
     , event_type TEXT
     , description TEXT
     , time_recorded TIMESTAMP
     , PRIMARY KEY (audit_id)
);

CREATE TABLE safeseed.group (
       group_id INT NOT NULL
     , name TEXT
     , description TEXT
     , PRIMARY KEY (group_id)
);

CREATE TABLE safeseed.global_seq (
       global_seq_id INT NOT NULL
     , locus TEXT
     , sequence TEXT
     , gi_number INT
     , from_field TEXT
     , name1 TEXT
     , name2 TEXT
     , description TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (global_seq_id)
);

CREATE TABLE safeseed.search_status (
       search_seq VARCHAR(50) NOT NULL
     , search_seq_full TEXT
     , status NUMERIC(1) DEFAULT 0
     , entry_time TIMESTAMP
     , checkout_time TIMESTAMP
     , finish_time TIMESTAMP
     , read_count INT DEFAULT 0
     , PRIMARY KEY (search_seq)
);

CREATE TABLE safeseed.person (
       person_id INT NOT NULL
     , username TEXT
     , guid TEXT
     , domain TEXT
     , email TEXT
     , PRIMARY KEY (person_id)
);

CREATE TABLE safeseed.seq (
       seq_id INT NOT NULL
     , person_id INT NOT NULL
     , sequence TEXT
     , name TEXT
     , description TEXT
     , species TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (seq_id)
     , CONSTRAINT FK_seq_1 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
);

CREATE TABLE safeseed.seq_frag (
       seq_frag_id INT NOT NULL
     , frag_length INT NOT NULL
     , seq_id INT NOT NULL
     , completed BOOLEAN
     , PRIMARY KEY (seq_frag_id)
     , CONSTRAINT FK_seq_frag_1 FOREIGN KEY (seq_id)
                  REFERENCES safeseed.seq (seq_id)
);

CREATE TABLE safeseed.global_seq_frag (
       global_seq_frag INT NOT NULL
     , global_seq_id INT NOT NULL
     , frag_length INT NOT NULL
     , completed BOOLEAN
     , date_completed TIMESTAMP
     , PRIMARY KEY (global_seq_frag)
     , CONSTRAINT FK_global_seq_frag_1 FOREIGN KEY (global_seq_id)
                  REFERENCES safeseed.global_seq (global_seq_id)
);

CREATE TABLE safeseed.global_history (
       global_history_id INT NOT NULL
     , global_seq_frag INT NOT NULL
     , person_id INT NOT NULL
     , params TEXT
     , notes TEXT
     , last_viewed TIMESTAMP
     , PRIMARY KEY (global_history_id)
     , CONSTRAINT FK_active_seqs_1 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_global_history_2 FOREIGN KEY (global_seq_frag)
                  REFERENCES safeseed.global_seq_frag (global_seq_frag)
);

CREATE TABLE safeseed.group_seq (
       group_id INT NOT NULL
     , seq_id INT NOT NULL
     , name TEXT
     , notes TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (group_id, seq_id)
     , CONSTRAINT FK_group_seq_1 FOREIGN KEY (group_id)
                  REFERENCES safeseed.group (group_id)
     , CONSTRAINT FK_group_seq_2 FOREIGN KEY (seq_id)
                  REFERENCES safeseed.seq (seq_id)
);

CREATE TABLE safeseed.person_group (
       person_id INT NOT NULL
     , group_id INT NOT NULL
     , role TEXT
     , PRIMARY KEY (person_id, group_id)
     , CONSTRAINT FK_person_group_1 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_person_group_2 FOREIGN KEY (group_id)
                  REFERENCES safeseed.group (group_id)
);

CREATE TABLE safeseed.queue (
       queue_id INT NOT NULL
     , seq_frag_id INT NOT NULL
     , complete BOOLEAN
     , date_completed TIMESTAMP
     , log_text TEXT
     , priority_level INT NOT NULL
     , PRIMARY KEY (queue_id)
     , CONSTRAINT FK_queue_1 FOREIGN KEY (seq_frag_id)
                  REFERENCES safeseed.seq_frag (seq_frag_id)
);

CREATE TABLE safeseed.submission (
       submission_id INT NOT NULL
     , person_id INT NOT NULL
     , seq_frag_id INT NOT NULL
     , seq_id INT NOT NULL
     , frag_length INT
     , date_requested TIMESTAMP
     , PRIMARY KEY (submission_id)
     , CONSTRAINT FK_submission_2 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_submission_3 FOREIGN KEY (seq_frag_id)
                  REFERENCES safeseed.seq_frag (seq_frag_id)
     , CONSTRAINT FK_submission_4 FOREIGN KEY (seq_id)
                  REFERENCES safeseed.seq (seq_id)
);

CREATE TABLE safeseed.seq_relationship (
       seq_id INT NOT NULL
     , global_seq_id INT NOT NULL
     , note TEXT
     , type TEXT
     , PRIMARY KEY (seq_id, global_seq_id)
     , CONSTRAINT FK_seq_relationship_1 FOREIGN KEY (seq_id)
                  REFERENCES safeseed.seq (seq_id)
     , CONSTRAINT FK_seq_relationship_2 FOREIGN KEY (global_seq_id)
                  REFERENCES safeseed.global_seq (global_seq_id)
);

CREATE TABLE safeseed.collection (
       person_id INT NOT NULL
     , seq_id INT NOT NULL
     , name TEXT
     , notes TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (person_id, seq_id)
     , CONSTRAINT FK_TABLE_17_1 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_TABLE_17_2 FOREIGN KEY (seq_id)
                  REFERENCES safeseed.seq (seq_id)
);

CREATE TABLE safeseed.global_collection (
       person_id INT NOT NULL
     , global_seq_id INT NOT NULL
     , label CHAR(10)
     , notes TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (person_id)
     , CONSTRAINT FK_TABLE_16_2 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_global_collection_2 FOREIGN KEY (global_seq_id)
                  REFERENCES safeseed.global_seq (global_seq_id)
);

CREATE TABLE safeseed.history (
       history_id INT NOT NULL
     , person_id INT NOT NULL
     , seq_frag_id INT NOT NULL
     , params TEXT
     , notes TEXT
     , last_viewed TIMESTAMP
     , PRIMARY KEY (history_id)
     , CONSTRAINT FK_history_1 FOREIGN KEY (person_id)
                  REFERENCES safeseed.person (person_id)
     , CONSTRAINT FK_history_2 FOREIGN KEY (seq_frag_id)
                  REFERENCES safeseed.seq_frag (seq_frag_id)
);

CREATE TABLE safeseed.global_frag (
       global_frag_id INT NOT NULL
     , start_seq TEXT
     , gc_content_percentage NUMERIC
     , three CHAR(1)
     , four CHAR(1)
     , nineteen CHAR(1)
     , twenty CHAR(1)
     , three_plus_four INT
     , pots_nt TEXT
     , global_seq_frag INT NOT NULL
     , PRIMARY KEY (global_frag_id)
     , CONSTRAINT FK_global_frag_1 FOREIGN KEY (global_seq_frag)
                  REFERENCES safeseed.global_seq_frag (global_seq_frag)
);

CREATE TABLE safeseed.group_global_seq (
       group_id INT NOT NULL
     , global_seq_id INT NOT NULL
     , label TEXT
     , notes TEXT
     , date_added TIMESTAMP
     , PRIMARY KEY (group_id, global_seq_id)
     , CONSTRAINT FK_group_global_seq_1 FOREIGN KEY (group_id)
                  REFERENCES safeseed.group (group_id)
     , CONSTRAINT FK_group_global_seq_2 FOREIGN KEY (global_seq_id)
                  REFERENCES safeseed.global_seq (global_seq_id)
);

CREATE TABLE safeseed.search_result (
       search_result_id INT NOT NULL
     , search_seq VARCHAR(50)
     , search_seq_rc TEXT
     , result_seq TEXT
     , result_accession TEXT
     , result_accession_offset INT
     , result_offset INT
     , time_stamp TIMESTAMP
     , PRIMARY KEY (search_result_id)
     , CONSTRAINT FK_search_result_1 FOREIGN KEY (search_seq)
                  REFERENCES safeseed.search_status (search_seq)
);

CREATE TABLE safeseed.frag (
       frag_id INT NOT NULL
     , seq_frag_id INT NOT NULL
     , start_seq TEXT
     , gc_content_percentage NUMERIC
     , three CHAR(1)
     , four CHAR(1)
     , nineteen CHAR(1)
     , twenty CHAR(1)
     , three_plus_four INT
     , pots_nt TEXT
     , PRIMARY KEY (frag_id)
     , CONSTRAINT FK_frag_1 FOREIGN KEY (seq_frag_id)
                  REFERENCES safeseed.seq_frag (seq_frag_id)
);

create index idx_status on safeseed.search_status (search_seq,status);
