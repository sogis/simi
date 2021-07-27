## Install Simi DB

**On geoutil.verw.rootso.org**
```
ansible-playbook -i test  dbserver.yml --tags=create_db_simi
```

**Inside network Canton of Solothurn**
```
git clone git@github.com simi-so/simi.git
cd simi
```

Run all the scripts in webapp/doc/platform_schema/v1.1.x. Use DB role *admin* when running the scripts.
```
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/create-schema.sql
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/create-db.sql
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/10.create-db.sql
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/20.create-db.sql
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/30.create-db.sql
psql simi -h geodb-t.rootso.org -U bjsvwpfe -f webapp/doc/platform_schema/v1.1.x/postscript.sql
```
