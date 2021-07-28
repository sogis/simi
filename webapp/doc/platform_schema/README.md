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

## Data migration

The migration takes place locally on your own computer.It is expected that you already have a database running and that you have imported the Config-DB-Dump there (DB-name config, schema gdi_knoten and iam)  

The environment variables for GRETL must be set as follows: 
dbUriConfig=jdbc:postgresql://IP:PORT/config 
dbUserConfig=USER
dbPwdConfig=PASSWORD
=> The same for dbUriSimi

1. Run the Script mig_trafo/ddl/create_all_tables.sql All tables are created in the public schema.
2. If you have to clean the public schema, you can run the script mig_trafo/ddl/delete_all_tables.sql and then again the create_all_tables.sql.  
3. Create the views required for migration in the gdi_knoten: 
    - mig_trafo/Migrations_views/permissions_view.sql
    - mig_trafo/Migrations_views/wfs_layer_v.sql
    - mig_trafo/Migrations_views/wms_layer_v.sql
4. Now you should be able to run the GRETL job to migrate the IAM: mig_trafo/migration_iam/build.gradle. 
5. Next you can run the migration of the data. This will take a few seconds longer. mig_trafo/migration_data/build.gradle.
6. The last thing to do is run the cleanup script. This fixes various "imperfections": mig_trafo/migration_data/aufraeumen.sql

**After this actual migration, the data must still be transferred to the "productive Simi-DB".**

This work is only semi-automatic. In the following build.gradle, the tasks must be commented out and back in one after the other: mig_trafo/mig_to_web_db/build.gradle
1. First, the whole IAM block can be copied without the permissions.
2. Then the data must be copied. To do this, comment out and comment out one line at a time.
3. Finally, the permissions can be copied.  

That's it. 
