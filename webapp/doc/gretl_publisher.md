# Services für GRETL Publisher

## Überblick

Der Publisher nutzt die folgenden drei Endpunkte von SIMI:
* Token-Service unter app/rest/v2/oauth/token: Zwecks Ausstellung eines OAuth-Zugriffstokens für die folgenden Requests. Der Token-Service ist Teil des Cuba-Frameworks, auf welchem SIMI basiert.
* Metadaten-Services:
    * Publikations-Service unter app/rest/pubsignal: Mit diesem wird im Request das Datum der aktuellsten Publikation einer Themenbereitstellung übermittelt.
    * Datenblatt-Service unter app/rest/doc: Generiert das Datenblatt und retourniert dieses mit der Response.

Die Aufrufe des Publisher können wie folgt mittels **curl** nachgestellt werden.

## Konfiguration

Token-Service:
* URL: Standard-URL von Framework übernommen (app/rest/v2/oauth/token)
* Benutzername: Siehe ENV-Variable CUBA_REST_CLIENT_ID
* Passwort: Siehe ENV-Varaible CUBA_REST_CLIENT_SECRET

Metadaten-Services:
* URL: app/rest/pubsignal respektive app/rest/doc
* Benutzername und Passwort: Der Benutzer für die Metadaten-Services wird analog einem "natürlichen" Benutzer via SIMI-Admin GUI angelegt. Es wird derselbe Benutzer für beide Endpunkte verwendet.

## Simulieren des Publisher-Client mittels CURL

Urls, Benutzernamen und Passwörter sind Teil der Konfiguration. Jeder CURL-Aufruf wird darum zuerst mit Platzhaltern für die Variablen a la $CUBA_REST_CLIENT_ID, und anschliessend mit einem konkreten Beispiel beschrieben. 

### Rückgabe des Security-Token für den REST-API Zugriff:

POST für Ausstellung der Tickets:

    curl -X POST \
      $SIMI_BASE_URL/rest/v2/oauth/token \
      -u '$CUBA_REST_CLIENT_ID:$CUBA_REST_CLIENT_SECRET' \
      -H 'Content-Type: application/x-www-form-urlencoded' \
      -d 'grant_type=password&username=$META_SERVICE_USER&password=$META_SERVICE_PASS'
      
Beispiel:

    curl -X POST \
      http://localhost:8080/app/rest/v2/oauth/token \
      -u 'notifier:secret' \
      -H 'Content-Type: application/x-www-form-urlencoded' \
      -d 'grant_type=password&username=gretl&password=pass'

Antwort mit "access_token":

    {"access_token":"n0rri21Kxbuekf2wuSPj0NSuMQw","token_type":"bearer","refresh_token":"lPgeTNwWTajKE1LEva4TkpHsSk4","expires_in":43199,"scope":"rest-api"}
    
Das zurückgegebene access_token wird folgend **$ACCESS_TOKEN** genannt

### Publikations-Service
   
    curl -X PUT -i \
    -H 'Authorization: Bearer $ACCESS_TOKEN' \
    -H "Content-Type: application/json" \
    -d '{
            "dataIdent": "$THEMEN_BEREITSTELLUNG",
            "published": "2021-12-23T14:54:49.050062",
            "publishedBaskets": [{
                "model": "$MODELL_NAME",
                "topic": "kantonsstrassen",
                "basket": "kantonsstrassen"
            }]
        }' \
    '$SIMI_BASE_URL/rest/pubsignal'

Beispiel:
    
    curl -X PUT -i \
    -H 'Authorization: Bearer n0rri21Kxbuekf2wuSPj0NSuMQw' \
    -H "Content-Type: application/json" \
    -d '{
            "dataIdent": "ch.so.avt.kantonsstrassen",
            "published": "2021-12-23T14:54:49.050062",
            "publishedBaskets": [{
                "model": "SO_AVT_Kantonsstrassen_Publikation_20200707",
                "topic": "kantonsstrassen",
                "basket": "kantonsstrassen"
            }]
        }' \
    'http://localhost:8080/app/rest/pubsignal'

Bei Erfolg wird Code 200 und die Anzahl der db updates and inserts zurückgegeben.
Bei Fehlern wird der treffende Status-Code <> 200 sowie eine Fehlermeldung zurückgegeben.

Aufruf mit Regionen siehe Doku des Publishers in Gretl-Repo.

Simi-Log bei Erfolg (Log-Level INFO):

        ...INFO ch.so.agi.simi.web.api.themepub.PubSignalController - Updated ch.so.avt.kantonsstrassen. Executed 0 db insert(s) and 1 db update(s).

Beispiel bei Fehler (Ausschnitt. Log-Level WARN):

        ...WARN ch.so.agi.simi.web.api.themepub.ExcConverter - ThemePublication reference is not known. Optional[getSingleResult() did not retrieve any entities.]. Inner Exception: Optional.empty Inner exc. message: Optional.empty

### Datenblatt-Service

    curl -X GET \
    -H 'Authorization: Bearer $ACCESS_TOKEN' \
    -H "Content-Type: text/html" \
    '$SIMI_BASE_URL/rest/doc?dataident=$THEMEN_BEREITSTELLUNG'
    
Beispiel:

    curl -X GET \
    -H 'Authorization: Bearer n0rri21Kxbuekf2wuSPj0NSuMQw' \
    -H "Content-Type: text/html" \
    'http://localhost:8080/app/rest/doc?dataident=ch.so.avt.kantonsstrassen'
    
Bei Erfolg wird Code 200 und die Datenbeschreibung als HTML-Seite zurückgegeben.
Bei Fehlern wird der treffende Status-Code <> 200 sowie eine Fehlermeldung zurückgegeben.

Simi-Log bei Erfolg (Log-Level Info):

        ...INFO ch.so.agi.simi.web.api.themepub.DataDocController - Returned doc for identifier: ch.so.avt.kantonsstrassen
        
Beispiel bei Fehler (Ausschnitt):

        ...WARN ch.so.agi.simi.web.api.themepub.ExcConverter - ThemePublication reference is not known. Optional[getSingleResult() did not retrieve any entities.]. Inner Exception: Optional.empty Inner exc. message: Optional.empty
