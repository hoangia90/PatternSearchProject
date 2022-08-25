-- Database: patternSearchdb

-- DROP DATABASE patternSearchdb;

CREATE DATABASE patternSearchdb
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
CREATE TABLE IF NOT EXISTS public.driving_license
(
    driving_license_id integer NOT NULL DEFAULT nextval('driving_license_driving_license_id_seq'::regclass),
    created_date character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    driving_license_no character varying(255) COLLATE pg_catalog."default",
    partnerid character varying(255) COLLATE pg_catalog."default",
    status integer,
    updated_date character varying(255) COLLATE pg_catalog."default",
    partner_id character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT driving_license_pkey PRIMARY KEY (driving_license_id)
)
