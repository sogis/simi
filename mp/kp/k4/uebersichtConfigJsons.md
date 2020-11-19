# Config-Jsons: Übersicht und zukünftige Pflege im Kontext "SIMI"

|Name|Nachführung|Bemerkungen|
|agdiConfig.json|Fällt weg||
|cccConfig.json|SIMI|Nachführung in SIMI führt zu höherer Transparenz er Abhängigkeit gegenüber der heutigen Lösung mit ENV-Variablen|
|documentConfig.json|?|Generieraufwand abschätzen. Falls nicht gross: In "no frills maske" von SIMI integrieren und rausgenerieren.|
|dataConfig.json|SIMI||
|dataproductConfig.json|SIMI|Insbesondere aufgrund der enthaltenen QML "gross"|
|elevationConfig.json|Datei direkt|Master der Datei im Pipeline Github Repo| 
|featureInfoConfig.json|SIMI|Referenzen auf die Reports werden in SIMI in vereinfachter Form als String-Feld geführt|
|landregConfig.json|Datei direkt|Siehe Bemerkung zu elevationConfig.json|
|legendConf.json|SIMI||
|mapinfoConfig.json|Datei direkt|Inhalt mapInfo-Service: Abfrage des Gemeindenames für übergebene Koordinate|
|mapViewConfig.json|SIMI|Config umfasst sowohl viel statisches und "dynamisches" aus SIMI --> Template in Pipeline-Repo, welches mittels sql2json in vollständiges json mit SIMI-Inhalten "aufgepumpt" wird|
|ogcConfig.json|SIMI||
|permalinkConfig.json|Datei direkt|Master in Pipeline github repo. |
|permissions.json|SIMI||
|plotinfoConfig.json|Datei direkt|Siehe Bemerkung zu elevationConfig.json|

|printConfig.json|?|Siehe Detailspez sourcepole|
|samlAuthConfig.json|Datei direkt|Siehe Bemerkung zu elevationConfig.json|
|searchConfig.json|


# Bemerkungen
* In den simplen json referenzierte Datasets werden als Abhängigkeit mittels Teilmodell "Dependency" erfasst.
Beispiel "Modul elevationService" ist abhängig von "Dataset DTM 2014 (Lidar)"

# Fragen
* permalinkConfig.json: Tabelle in SIMI-DB aufnehmen oder separat lassen?

# Detailpez sourcepole

dataproductConfig.json
* Enthaltene QML in Config werden base64 codiert. Service Response muss aber wie bis anhin codiert sein (ohne base64)

legendConf.json
* Legendenbilder neu direkt als base64 eincodiert.
* Frage: Service ist Teil von "GetLegendInfo" des WMS, korrekt?

printConfig.json
* Zzusammenhänge verstehen:
  * Zum WGC (Auswahl der Print-Layouts)
  * Zum qgs: Da mittels QGIS-Server zusatzfunktionalität gedruckt wird, muss dem QGIS-Server die verfügbaren Layouts ebenfalls bekannt sein.

