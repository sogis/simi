# Simi Umsetzungsfragen

## Vererbungstrategien pro Baum

Teilmodell|Root-Klasse|Strategie|Bemerkungen|
|---|---|---|---|
|Contact|Contact|Joined|Wegen Beziehung zwischen den Kindern|
|Data|DataSetView|Joined|Wegen Beziehung zwischen den Kindern|
|Data|DataSet|"Konzeptionell"|Physische Abbildung weder sinnvoll noch notwendig|
|Data|TableDS|SingleTable|Ist einfacher...|
|Data|TableDS|SingleTable|Ist einfacher...|
|IAM|Identity|Joined|Wegen Beziehung zwischen den Kindern|
|Product|Dataproduct|Strukt|Wegen Vererbungstiefe nicht "joined"|
|Product|SingleActor|Joined|Wenn es nicht performt, Umbau auf SingleTable|
|Product|ProductList|SingleTable|Ist einfacher...|




