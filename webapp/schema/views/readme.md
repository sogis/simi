# Views in Simi-DB

In der Simi-DB sind Views für den Export von Metadaten als json (Trafo), die Suche (solr) sowie die Ausgabe / Validierung
von komplexen Zusammenhängen im SIMI selbst (in der Webapplikation).

Das folgende Diagramm dokumentiert die Abhängigkeiten der Views untereinander.

![view_dependencies](doc_resources/view_dependencies.png)

## Aktualisierung aller Views mit einem Command in einer Transaktion

Bei jedem Ausführen der automatischen Migrationsskripte, weil die Abhängigkeiten vom Framework mit "cascade" gelöscht werden.

Den folgenden Befehlt verwenden, um alle Views wieder herzustellen (Nach git repo pull):

```bash
psql -d simi -h host -U user -W \
  --single-transaction \
\
  -f trafo/drop_trafo_views.sql \
  -f solr/drop_solr_views.sql \
  -f app/drop_app_views.sql \
\
  -f trafo/trafo_published_dp_v.sql \
  -f trafo/trafo_resource_role_v.sql \
  -f trafo/trafo_wms_geotable_v.sql \
  -f trafo/trafo_wms_tableview_v.sql \
  -f trafo/trafo_tableview_attr_with_geo_v.sql \
  -f trafo/trafo_wms_layer_v.sql \
  -f trafo/trafo_wms_rootlayer_v.sql \
\
  -f solr/solr_layer_base_v.sql \
  -f solr/solr_layer_background_v.sql \
  -f solr/solr_layer_foreground_v.sql \
\
  -f app/app_themepub_v.sql \
\
  -f trafo/grant_trafo_views.sql \
  -f solr/grant_solr_views.sql \
  -f app/grant_app_views.sql
```