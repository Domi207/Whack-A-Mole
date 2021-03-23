# Whack-A-Mole

Whack A Mole ist ein Plugin mit dem du Whack A Mole spielen kannst.

[Video Anleitung](https://www.youtube.com/watch?v=xaazUgEKuVA)
[Builded Jar Download](https://github.com/Domi207/Whack-A-Mole/tree/master/dist)

## Einrichtung

Nutze diese Commands um das Plugin einzurichten:
```plain
/whackamule settings center
/whackamule settings radius <radius>
/whackamule settings pointsPerMule <pointsPerMule>
/whackamule settings minusPoints <minusPoints>
/whackamule settings lossPerSecond <lossPerSecond>
/whackamule settings time <time>
/whackamule settings spawnPercent <spawnPercent>
```

## GUI
![alt text](https://github.com/Domi207/Whack-A-Mole/blob/master/pictures/GUI-1.png?raw=true)

## Leaderboard
Das Leaderboard wird nach den folgenden Bedingungen generiert:
1. Punkte
2. Zeitpunkt des Erreichens

Wenn der Zeitpunkt auch gleich ist hat eindeutig jemand an der leaderboard.yml herumgespielt.

## Command
Der Basis Command laute:
```
/whackamole | /wam
```
Untercommands:
```
play | start: Startet das Spiel
stop: Stoppt das Spiel
settings: Richtet das Plugin ein
    center: Setzt den Spielfeld Mittelpunkt auf die Aktuelle Position
    radius: Setzt den Spielfeld Radius um den Mittelpunkt
    pointsPerMule: Setzt die Basispunktanzahl per geschlagenen Maulwurf
    lossPerSecond: Setzt die Verlustrate pro Sekunde nach dem erscheinen das Maulwurfs
    time: Setzt die Spielzeit
    spawnPercent: Setzt die Wahrscheinlichkeit, dass ein Maulwurf entsteht pro Viertel Sekunde
    minusPoints: Setzt die Minuspunkte für einen Verfehlten Schlag 

```
## Permissions:
```json
wam: Erlaubt das Spielen des Spieles und einsehen des Highscores
wam.setting: Erlaubt das Ändern der Spiel Einstellungen
wam.stop.other: Erlaubt das stopppen von Spielen, die man selbst nicht spielt
```

## Config
### config.yml
```json
pointsPerMule | Stellt die Punkte pro Maulwurf ein
lossPerSecond | Stellt die verluste pro Sekunde zu spät hauen ein
time | Stellt die Spielzeit pro Spiel ein
spawnPercent | Stellt die Spawn Wahrscheinlichtkeit pro vierteil Sekunde ein
minusPoints | Stellt die Minus Punkte pro Fehlschlag ein
headValue | Stellt den Texture Value für die Köpfe ein
center | Stellt den Center des Spielfeldes ein WICHTIG: Am besten per Command einfügen augrund des komplizierten Syntaxes
```
### leaderboard.yml
In dieser Datei werden die Highscores gespeichert. Am Besten nichts ändern.

### messages.yml
```
prefix: '§7[§aWhack§7-§bA§7-§cMole§7]§6: '
highscoreinventory:
  title: §aWhack§7-§bA§7-§cMole
  template: '§a%s§7: §b%s§8'
  loretemplate: §8%s
inventory:
  title: §aWhack§7-§bA§7-§cMole
  item:
    start:
      name: §r§a§lStarte ein neues Spiel
    cantStart:
      name: §r§c§lAktuell spielt bereits ein Spieler
    stop:
      name: §r§c§lStoppe das aktuelle Spiel
    highscores:
      name: §6§lHighscores
leaderboard:
  invalidvalue: §cDieser Wert ist ungültig
  maxvalue: §cDer Wert wurde auf 100 reduziert
  header: "§bDie Top §a%s§b von Whack-A-Mole:\n "
  template: '§6%s. §a%s§7: §b%s§8 - §7%s'
settings:
  center:
    sucess: §aDer Mittelpunkt wurde erfolgreich gesetzt
  radius:
    sucess: §aDer Radius wurde erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings radius <value>
  pointsPerMule:
    sucess: §aDer Punkte für jeden Schlag wurde erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings pointsPerMule <value>
  lossPerSecond:
    sucess: §aDie Verluste wurden erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings lossPerSecond <value>
  time:
    sucess: §aDie Spielzeit wurden erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings time <value>
  spawnPercent:
    sucess: §aDie Spawn Prozent wurden erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings spawnPercent <value>
  minusPoints:
    sucess: §aDie Minus Punkte wurden erfolgreich gesetzt
    invalidValue: §cDieser Wert ist ungültig
    syntax: §c/wrackamule settings spawnPercent <value>
  syntax: §c/wrackamule settings <center;radius;pointsPerMule;minusPoints;lossPerSecond;time>
    [value]
  noPermissions: §cDu darfst diesen Command nicht ausführen
start:
  alreadyRunning: §cEin Spieler spielt bereits
  pluginNotSetup: §cDas Plugin ist nicht fertig eingerichtet
stop:
  noPermissions: §cDu darfst das Spiel von anderen Spielern nicht beenden
  notRunning: §cEs läuft aktuell kein Spiel
  sucess: §aDas Spiel wurde erfolgreich beendet
noPermissions: §cDu hast keine Berechtigungen um diesen Command auszuführen
noPlayer: §cDas sind doch alles Bots
countdown:
  '3': §c§l3
  '2': §e§l2
  '1': §b§l1
  '0': §a§lGo!
gameEnd:
  normal:
    title: §c§lDas Spiel ist vorbei
    subtitle: §a%s Punkte
  highScore:
    title: §b§lNeuer Highscore
    subtitle: §a%s Punkte
game:
  actionbar: §a%s §b§l | §c%s Punkte

```