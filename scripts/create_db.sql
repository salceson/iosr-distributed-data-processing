CREATE TABLE public.carriers
(
  year integer NOT NULL,
  week integer NOT NULL,
  carrier character varying(20) NOT NULL,
  "from" character varying(20) NOT NULL,
  "to" character varying(20) NOT NULL,
  arrival_sum double precision,
  departure_sum double precision,
  num integer,
  CONSTRAINT carriers_pk PRIMARY KEY (year, week, carrier, "from", "to")
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.airports
(
  year integer NOT NULL,
  week integer NOT NULL,
  airport character varying(20) NOT NULL,
  num integer,
  CONSTRAINT airports_pk PRIMARY KEY (year, week, airport)
)
WITH (
  OIDS=FALSE
);

CREATE TABLE public.weekdays
(
  year integer NOT NULL,
  week integer NOT NULL,
  day_of_week integer NOT NULL,
  arrival_sum double precision,
  departure_sum double precision,
  num integer,
  CONSTRAINT weekdays_pk PRIMARY KEY (year, week, day_of_week)
)
WITH (
  OIDS=FALSE
);
