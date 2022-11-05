# Services für GRETL Publisher

Die Aufrufe des Publisher können wie folgt mittels **curl** nachgestellt werden.
Beim Publisher wird das gleiche Paar Benutzername/Passwort für Tokenausgabe und Identifikation des eigentlichen Service verwendet. In den folgenden CURL-Beispielen heisst der Benutzer **"notifier"** und das Passwort **"pass"**.

## Rückgabe des Security-Token für den REST-API Zugriff:

POST für Ausstellung der Tickets:

    curl -X POST \
      http://localhost:8080/app/rest/v2/oauth/token \
      -u 'notifier:pass' \
      -H 'Content-Type: application/x-www-form-urlencoded' \
      -d 'grant_type=password&username=notifier&password=pass'

Antwort mit "access_token":

    {"access_token":"n0rri21Kxbuekf2wuSPj0NSuMQw","token_type":"bearer","refresh_token":"lPgeTNwWTajKE1LEva4TkpHsSk4","expires_in":43199,"scope":"rest-api"}
    
Das zurückgegebene access_token wird folgend **\_access_token\_** genannt

## Endpunkt /pubsignal zur Publikations-Notifikation

Am Beispiel der Daten aus einem Integrationstest:
    
    curl -X PUT -i \
    -H 'Authorization: Bearer _access_token_' \
    -H "Content-Type: application/json" \
    -d '{
            "dataIdent": "inttest.publishsrv.NoRegion_NoSuffix_NoPsubs.suffix",
            "published": "2021-12-23T14:54:49.050062",
            "publishedBaskets": [{
                "model": "SO_AGI_MOpublic_20201009",
                "topic": "Bodenbedeckung",
                "basket": "oltenBID"
            }, {
                "model": "DM01",
                "topic": "Liegenschaften",
                "basket": "wangenBID"
            }]
        }' \
    'http://localhost:8080/app/rest/pubsignal'
    
Nach Einsetzen des access_token lautet die Header-Zeile Beispielsweise:

    -H 'Authorization: Bearer n0rri21Kxbuekf2wuSPj0NSuMQw' \

Bei Erfolg wird Code 200 und die Anzahl der db updates and inserts zurückgegeben.
Bei Fehlern wird der treffende Status-Code <> 200 sowie eine Fehlermeldung zurückgegeben.

Aufruf mit Regionen siehe Doku des Publishers in Gretl-Repo.

## Endpunkt /doc zur Rückgabe der Datenbeschreibung

    curl -X GET \
    -H 'Authorization: Bearer _access_token_' \
    -H "Content-Type: text/html" \
    'http://localhost:8080/app/rest/doc?dataident=inttest.publishsrv.NoRegion_NoSuffix_NoPsubs.suffix'
    
Bei Erfolg wird Code 200 und die Datenbeschreibung als HTML-Seite zurückgegeben.
Bei Fehlern wird der treffende Status-Code <> 200 sowie eine Fehlermeldung zurückgegeben.

