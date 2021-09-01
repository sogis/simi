SET role admin;

CREATE ROLE simi_write WITH
  NOLOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

GRANT simi_write TO simi;

CREATE SCHEMA simi
    AUTHORIZATION admin;

COMMENT ON SCHEMA simi
    IS 'Metadaten Werkzeug Simi Datenschema. Fragen: oliver.jeker@bd.so.ch';

GRANT USAGE ON SCHEMA simi TO PUBLIC;
GRANT ALL ON SCHEMA simi TO admin;
GRANT ALL ON SCHEMA simi TO simi_write;
