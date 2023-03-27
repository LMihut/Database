# Praktikum 5 Vorbereitung

## Alle Verwendeten Queries von Aufgabe 1

### 1. Ausgabe aller Spieler (Spielername), die in einem bestimmten Zeitraum gespielt hatten. 
SELECT DISTINCT t0.PLAYERID, t0.PLAYERNAME, t1.GAMEID 
FROM GAME t1 LEFT OUTER JOIN PLAYER t0 ON (t0.PLAYERID = t1.player) 
WHERE (t1.STARTTIME BETWEEN '2020-01-01 00:00:00.0' AND '2020-01-02 23:59:59.0') ORDER BY t1.GAMEID

### 2. Ausgabe zu einem bestimmten Spieler: Alle Spiele (Id, Datum), sowie die Anzahl der korrekten Antworten pro Spiel mit Angabe der Gesamtanzahl der Fragen pro Spiel bzw. alternativ den Prozentsatz der korrekt beantworteten Fragen. 
SELECT DISTINCT t0.GAMEID, t0.STARTTIME, COUNT(t1.QUESTIONID), SUM(CASE  WHEN (t2.QUESTIONANSWER = true) THEN 1 ELSE 0 END) FROM PLAYER t4, PLAYER t3, gameQuestion t2, QUESTION t1, GAME t0 WHERE ((t4.PLAYERNAME = '26') AND ((t4.PLAYERID = t0.player) AND ((t2.Game_GAMEID = t0.GAMEID) AND (t1.QUESTIONID = t2.questionAnswer_KEY)))) GROUP BY t0.GAMEID


### 3. Ausgabe aller Spieler mit Anzahl der gespielten Spiele, nach Anzahl absteigend geordnet. 
SELECT t0.PLAYERNAME, COUNT(t1.GAMEID)
FROM PLAYER t0, GAME t1
WHERE (t0.PLAYERID = t1.player)
GROUP BY t0.PLAYERNAME ORDER BY COUNT(t1.GAMEID) DESC

### 4. Ausgabe der am meisten gefragten Kategorie, oder alternativ, die Beliebtheit der Kategorien nach Anzahl der Auswahl absteigend sortiert. 
SELECT t0.NAME, t0.CATEGORYID, COUNT(t0.CATEGORYID) 
FROM CATEGORY t5, QUESTION t4, GAME t3, gameQuestion t2, QUESTION t1, CATEGORY t0 
WHERE (((t4.QUESTIONID = t1.QUESTIONID) AND (t5.CATEGORYID = t0.CATEGORYID)) AND (((t2.Game_GAMEID = t3.GAMEID) AND (t1.QUESTIONID = t2.questionAnswer_KEY)) AND (t5.CATEGORYID = t4.CATEGORY_CATEGORYID))) 
GROUP BY t0.CATEGORYID ORDER BY COUNT(t0.CATEGORYID) DESC
