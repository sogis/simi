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
Replace *username* with your username (needs to be member of role admin) and *dbname* with name of the DB Server where the simi DB runs.
```
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/create-schema.sql
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/create-db.sql
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/10.create-db.sql
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/20.create-db.sql
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/30.create-db.sql
psql simi -h dbname -U username -c 'set role admin;' -f webapp/doc/platform_schema/v1.1.x/postscript.sql
```
