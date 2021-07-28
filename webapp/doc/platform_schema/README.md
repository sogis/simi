## Install Simi DB

**On geoutil.verw.rootso.org**

Replace *env* with the name of the environment (*test*,*integration*,*production*)
```
ansible-playbook -i env  dbserver.yml --tags=create_db_simi
```

**Inside network Canton of Solothurn**
```
git clone git@github.com simi-so/simi.git
cd simi
```

Run all the scripts in webapp/doc/platform_schema/v1.1.x. Use DB role *admin* when running the scripts.
Replace *username* with your username (needs to be member of role admin) and *dbhost* with hostname of the DB Server where the simi DB runs.
```
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/create-schema.sql
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/create-db.sql
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/10.create-db.sql
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/20.create-db.sql
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/30.create-db.sql
psql simi -h dbhost -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/postscript.sql
```

To import data from soconfig DB you first have to export data from soconfig DB in a local development DB. 
Then generate a data dump of your local DB and import in an existing simi DB with 
```
sudo -u postgres pg_restore -d simi --single-transaction --clean /path/to/dump.dmp
```

Afterwards the table rights must eventually be corrected with

```
psql -h dbhost -c 'set role admin;' -f postscript.sql simi
```
