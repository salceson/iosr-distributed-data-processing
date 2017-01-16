CREATE TABLE public.flights
(
  year integer NOT NULL,
  week integer NOT NULL,
  carrier character varying(20) NOT NULL,
  "from" character varying(20) NOT NULL,
  "to" character varying(20) NOT NULL,
  arrival_sum double precision,
  departure_sum double precision,
  num integer,
  CONSTRAINT flights_pk PRIMARY KEY (year, week, carrier, "from", "to")
)
WITH (
  OIDS=FALSE
);
