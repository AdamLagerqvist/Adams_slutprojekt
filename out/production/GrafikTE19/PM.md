# Titel 

Adam Lagerqvist (2021-06-01). 

## Inledning

Målet med uppgiften för mig var att göra något kul samt jobba på någon större helhet istället för smådelar här och där samt att lära hur man strukturerar större projekt.

började med att planera arbetet första lektionen efter det har jag tagit saker bit för bit istället för att fokusera på helheten detta har lett till att jag har behållit motivationen kvar i mitt projekt och sett till att jag alltid viste vad jag skulle göra. 

## Bakgrund
jag började planera mitt arbete vad vill jag kunna göra i mitt spel hur ska jag göra det. Det jag kom fram till var att jag skulle ha ett spel där man styr satelliter som kretsar runt en planet. Satelliterna ska man använda sig av för att skydda sig mot asteroider som rör sig långsamt mot din planet. Jag ville göra detta med cirkulära hitboxes och använda mig av streams för första gången eftersom jag tyckte foreach loopen från javascript skulle vara användbar (vilket var javaskripts version av stream... typ) Sedan snart efter jag började skriva koden bestämde jag mig för att fokusera på struktur och läsbarhet vilket hjälpte mig senare i projektet (ändrade namn på allt och skapade fler metoder).

Det första jag gjorde inom kod väg var att se till att grafiken fungerade för att göra detta snodd... jag menar lånade jag din kod Magnus för att ha den som en basline till hur min grafik fungerar. Efter detta fixade jag asteroiderna och cirklarna så att jag skulle kunna testa hur/om mitt koalitions system skulle fungera. Efter jag fått koalitionerna att fungera fokuserade jag på att fixa så att satteliterna fungerade och tillslut skapade jag ett highscore system till mitt spel. Och sist men inte minst skapade jag också ett title screen och en how to play sida.

## Positiva erfarenheter

Det har varigt kul att jobba på något spel aktigt samt ett större projekt.
jag har lärt mig mycket om hur man gör vissa saker i java.

## Negativa erfarenheter

Kan ibland vara frustrerande hur vissa saker fungerar i java. ex:
``` 
asteroids.stream().forEach(asteroid -> {
                asteroid.move();
                if (checkEarthCollision(asteroid)) {
                    asteroidsToRemove.add(asteroid);
                    loose();
                }
                Optional<Satelite> collidingSatelite = checkCollisions(asteroid, satelites);
                if (collidingSatelite.isPresent()) {
                    // Handle satelite collision.
                    // Add damage to satelite
                    // Destroy asteroid.
                    asteroidsToRemove.add(asteroid);
                    score++;
                }
            });
            asteroids.removeAll(asteroidsToRemove);
```
Hur man inte kan vara och rota i listan medans man stremar den så jag måste ha en till lista för att sedan tabort asteroiderna med senare i programmet. ## Sammanfattning Jag tycker uppgiften har varigt rolig att arbeta med och jag har lärt mig mycket. Jag är även väldigt nöjd med slut produkten.