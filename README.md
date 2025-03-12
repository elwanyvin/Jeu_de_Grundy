Grundy_Game

My versions of the Game of Grundy, created in cooperation with Emile Thevenin during my training at the IUT of Vannes. There are five versions, each one being more optimized in terms of execution time and number of operations than the previous one.

The main objective of this project was to implement different algorithms to solve the same problem. By comparing distinct algorithmic approaches, we aimed to highlight the execution speed of different algorithms through empirical measurements.

See the PDF for the complexity charts of each version.

Below is the documentation for each version, prepared by our professor.

Version 0

Version 0 is a basic recursive version (called GrundyRecBruteEff.java) that contains a fundamental method in a "player versus machine with AI" game:

boolean jouerGagnant (ArrayList jeu).

Given the state of the game passed as a parameter to the method (of type ArrayList), this method returns a boolean that is true if the next move (i.e., the next decomposition into several piles) is 100% winning for the machine (in which case, the machine plays the winning move). If this boolean is false, the machine plays randomly.

This method (jouerGagnant) is based on two fundamental rules derived from this document:
(http://mathenjeans.free.fr/amej/edition/9903grun/grundy2.html):

A situation (or position) is said to be losing for the machine (or the player, it doesn't matter) if and only if all its possible decompositions (i.e., all the actions that consist of splitting a pile into two unequal piles) are all winning for the opponent. In other words, whatever action is taken, none will prevent the opponent from winning.

A situation (or position) is said to be winning for the machine (or the player, it doesn't matter) if there is at least one decomposition (i.e., at least one action that consists of breaking down a pile into two unequal piles) that is losing for the opponent.

The jouerGagnant method calls the estPerdante method on all possible decompositions. As soon as a decomposition is losing (for the opponent), it is played (by the machine), ensuring victory, and the method returns true. Otherwise, the method returns false (none of the decompositions are losing for the opponent), and the machine does not perform any decomposition.

Only the estPerdante method is recursive.

This version (as well as all subsequent ones) also includes an efficiency test that measures the execution time and the number of operations when the computer plays.

Version 1

Version 1 is more efficient than Version 0, based on an obvious observation: there is no need to recalculate the decompositions of a pile (or situation) that we already know (because it has been previously evaluated) to be losing. These losing heaps (situations) are stored in an array (ArrayList) that can be checked for each pile and situation before even starting a recursive decomposition (which would otherwise be a waste of time).

Other simplifications to the table of losing positions:

It only keeps piles greater than 2 (since [1] and [2] cannot be split).

A losing situation appears only once in the table.

Each pile in a losing situation is sorted in ascending order for easier search implementation.

Version 2

Version 2, more efficient than Version 1, utilizes a table of losing positions (like Version 1) but also a table of winning positions. These losing and winning situations are stored in two separate tables (one for losers, one for winners) that can be consulted for each situation before starting a recursive decomposition, thus saving time.

Version 3

Version 3 is based on theorem 3.4 (http://mathenjeans.free.fr/amej/edition/9903grun/grundy2.html), which states:

The nature of a situation does not change if you add or remove a losing pile (which acts as neutral).

In other words:

Losing situation (L) + losing pile (L) = losing situation (L)

Winning situation (G) + losing pile (L) = winning situation (G)

Example: The situation [3, 4, 20] is equivalent to [3] because:

[4] is losing, so [3] (G) + [4] (L) = [3] (G)

[20] is losing, and since it has already been evaluated as a loser in the "perdants" table, we have [3] (G) + [20] (L) = [3] (G)

This theory eliminates the need to recalculate already known positions. If [3] is known as a winning position, then when the program generates [3, 4, 20], it immediately knows this is a winning position and removes [4] and [20] (since both are losers).

Version 3 builds the losing and winning tables like Version 2 and additionally uses theorem 3.4. It is therefore more efficient than Version 2.

Version 4

Version 4 is based on the following additional rules:

Rule 1: Winning pile1 (G) + Winning pile2 (G) = Winning pile (G) if and only if winner1 and winner2 are of different types.

Rule 2: Winning pile1 (G) + Winning pile2 (G) = Losing pile (L) if and only if winner1 and winner2 are of the same type.

To determine if two positions (heaps) are of the same type, we use the following predefined array:

int[] type = {0,0,0,1,0,2,1,0,2,1,0,2,1,3,2,1,3,2,4,3,0,4,3,0,4,3,0,4,1,2,3,1,2,4,1,2,4,1,2,4,1,5,4,1,5,4,1,5,4,1,0};

Each integer in this array represents the type of its corresponding index.

Examples:

[2] and [4] are of the same type T0. However, they are both losers, so none of the above rules apply.

[9] and [15] are of the same type T1 and both winners. Thus, the situation [9, 15] is losing (see Rule 2). Consequently, [11, 9, 15] simplifies to [11], since [9, 15] is neutral.

This approach helps eliminate pairs of winning positions that together form a losing one (e.g., X + 9 + 15 = X). This version constructs the losing and winning tables like Version 2, implements theorem 3.4 from Version 3, and applies the type theory explained above, making it the most efficient version.

