# Repo Inhalt

Das Repo umfasst die folgenden Teile (in Unterordnern):
* [/webapp](webapp): Source-Code der Metadatenpflegeapplikation "simi", inkl Docker Image Definition.
* [/modeldoc](modeldoc): Dokumentation des Meta-Datenmodells

Templates für den Betrieb in OpenShift sind unter
https://github.com/sogis/openshift-templates/tree/master/simi
und https://github.com/sogis/openshift-templates/tree/master/simi-schemareader.

# Branching

Neue Versionen werden auf dem Master-Branch entwickelt und zur Release-Reife gebracht.

Nach der Version benannte Maintentance-Branches existieren temporär in "Zeitfenstern", in welchen eine neu auf dem Master entstehende Version noch nicht produktiv ist. Beispiel für Maintenance-Branch: v2.0.x
