# Jeu_de_Grundy
Mes version du Jeu de Grundy, réalisées en coopération avec @KeroZd, au cours de ma formation à l'IUT de Vannes. Il y a  5 version, chaque version étant plus optimisée en temps et en nombre d'opérations que la précédente.

L’objectif principal de ce projet était la mise en œuvre de différents algorithmes pour résoudre un même problème.
Par comparaison d’approches algorithmiques distinctes, il était demandé de mettre en évidence par
des mesures empiriques la rapidité d’exécution des différents algorithmes.

Voir le PDF pour les graphiques de complexité de chaque version.
Ci-dessous la documentation de chaque version, réalisée par notre profeseur.

# Version 0
La version 0 est une version récursive de base (appelée GrundyRecBruteEff.java) qui
contient une méthode fondamentale dans un jeu « joueur contre machine avec IA » et qui se
nomme : boolean jouerGagnant ( ArrayList<Integer> jeu ).
Etant donné l’état du jeu passé en paramètre à la méthode (type ArrayList), cette méthode renvoie
un booléen qui est à vrai si le coup suivant (i.e. la décomposition suivante en plusieurs tas
d’allumettes) est gagnant à 100% pour la machine (et dans ce cas le coup gagnant est joué par la
machine). Si ce booléen est à faux, la machine jouera au hasard.
Cette méthode (jouerGagnant) se base sur 2 règles fondamentales qui proviennent de ce document
(http://mathenjeans.free.fr/amej/edition/9903grun/grundy2.html) et qui sont :
1. Une situation (ou position) est dite perdante pour la machine (ou le joueur, peu importe) si
et seulement si TOUTES ses décompositions possibles (c-à-d TOUTES les actions qui
consistent à décomposer un tas en 2 tas inégaux) sont TOUTES gagnantes pour l’adversaire.
En d’autres mots, quel que soit l’action mise en œuvre (parmi toutes celles qui sont possibles),
AUCUNE n’empêchera l’adversaire de gagner.
2. Une situation (ou position) est dite gagnante pour la machine (ou le joueur, peu importe),
s’il existe AU MOINS UNE décomposition (c-à-d UNE action qui consiste à décomposer un tas
en 2 tas inégaux) perdante pour l’adversaire.
La méthode jouerGagnant appelle la méthode estPerdante sur toutes les décompositions possibles.
Dès qu’une décomposition est perdante (pour l’adversaire), elle est jouée (par la machine), la
machine est sûre de gagner et la méthode renvoie vrai. Sinon la méthode renvoie faux (aucune
décomposition n’est perdante pour l’adversaire) et la machine n’effectue aucune décomposition.
Seule la méthode estPerdante est récursive.
Cette version (ainsi que toutes les suivantes) contient également un test d'efficacité qui mesure le temps d'éxécution et le nombre d'opérations lorsque l'ordinateur joue.

# Version 1

Cette version1, plus efficace que la version0, repose sur une constatation assez évidente : il ne sert
à rien de recalculer les décompositions d’un tas (ou situation) que l’on sait (car déjà évalué) de toute
façon perdant(e). Ces tas (situations) perdants sont stockés dans un tableau (ArrayList) qu’il faudra
simplement consulter pour chaque tas et situations avant même d’entamer une décomposition
récursive (qui ne sert à rien sauf à perdre du temps).

Autres simplifications au tableau des situations perdantes : il ne conserve que des tas
d’allumettes > 2 (car [1] et [2] ne peuvent pas être séparés), une situation perdante n’apparaît
qu’en un seul exemplaire dans le tableau et chacun des tas qui la composent sont rangés par
ordre croissant des valeurs (pour permettre une recherche + facile à mettre en oeuvre).

# Version 2

Cette version2, plus efficace que la version1, exploite un tableau des positions perdantes (comme
la version1) mais aussi un tableau des positions gagnantes. Ces situations perdantes et gagnantes
sont stockées dans 2 tableaux distincts (un perdant, un gagnant) qu’il faut simplement consulter
pour chaque situation avant même d’entamer une décomposition récursive (d’où le gain de temps).

# Version 3

Cette version3 se base sur le théorème 3.4
(http://mathenjeans.free.fr/amej/edition/9903grun/grundy2.html) qui est le suivant : la nature d'une
situation ne change pas si on lui ajoute ou si on lui enlève un tas perdant (qui joue le rôle de neutre).
En d’autres termes :
• situation Perdante (P) + tas Perdant (P) = situation Perdante (P)
• situation Gagnante (G) + tas Perdant (P) = situation Gagnante (G)
Exemple : la situation [3, 4, 20] est équivalente à [3] car : [4] est perdant donc [3] (G) + [4] (P) = [3]
(G) et comme [20] est perdant (car supposé déjà évalué et mémorisé comme perdant dans le tableau
des perdants) [3] (G) + [20] (P) = [3] (G).
Cette théorie permet de ne pas recalculer des positions déjà connues. Par exemple si [3] est connu
comme une position gagnante, quand le programme génère la situation [3, 4, 20], cette situation
sera immédiatement connue comme gagnante et on supprimera [4] et [20] (car tous les 2 perdants).
Cette version3 construit les tableaux perdants et gagnants comme pour la version2 ET utilise en plus
le théorème 3.4. Elle est donc + efficace que la version2.

# Version 4

Cette version4 se base sur les constatations supplémentaires suivantes :
• Règle 1 : tas gagnant1 (G) + tas gagnant2 (G) = tas gagnant (G) si et seulement si gagnant1
et gagnant2 sont de types différents.
• Règle 2 : tas gagnant1 (G) + tas gagnant2 (G) = tas perdant (P) si et seulement si gagnant1 et
gagnant2 sont de même type.
Pour savoir si deux positions (tas) sont de même type, on se base sur le tableau ci-dessous qui nous a
été donné :
int [] type = {0,0,0,1,0,2,1,0,2,1,0,2,1,3,2,1,3,2,4,3,0,4,3,0,4,3,0,4,1,2,3,1,2,4,1,2,4,1,2,4,1,5,4,1,5,4,1,5,4,1,0};
Où chaque entier est le type de son indice dans le tableau.

Exemples :
• [2] et [4] sont de même type T0. Mais attention, ils sont en plus tous les 2 perdants donc aucune
des 2 règles ne peut s’appliquer.
• [9] et [15] sont de même type T1. Et ils sont en plus tous les 2 gagnants. Donc la situation [9,
15] est perdante (voir la règle ci-dessus). La situation [11, 9, 15] est donc simplifiable en [11]
puisque [9, 15] est neutre.
• Le même principe s’applique pour [27] et [39] : [27, 39] est perdant car [27] et [39] sont de
même type T4 et tous les deux gagnants.
• Par contre, [3, 5, 13] n'est pas du tout simplifiable car les trois tas ne sont pas de même type
([3] est T1, [5] est T2 et [13] est T3).
L’idée est donc de supprimer les couples de positions gagnantes qui ensemble sont perdantes : par
exemple X + 9 + 15 = X.
De cette manière, il va être facile de savoir si deux tas sont perdants : soit un tas de « i » allumettes
et un tas de « j » allumettes, si type[i] == type[j] alors cela signifie que les tas « i » et « j »
sont de même type et donc ils sont forcément perdants ensemble (car P + P = P et G + G de même
type = P).
Cette version4 construit les tableaux perdants et gagnants comme pour la version2 ET tient compte
du théorème 3.4 de la version3 ET met en œuvre la théorie sur les types expliquée ci-dessus.

