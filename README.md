A Crossword Puzzle Solver AI

This program is capable of solving the crossword puzzle that could be found in the "crossword.txt".

How it works:
Program has the info of the all possible words stored in their respective "nLengthWords" array.
AI checks the horizontal space at (0,0) (top left of the Word Field)
Picks a word from the respective nLengthWords array with that length info and types it while removing that from the actual array so it can't be used again.
Does that a couple times and then checks the (0,0) vertically from top to bottom until it hits a barrier (@ char). Then uses that length & char info to check if theres a word existing within the Word Arrays.
If that word doesn't exist, it resets the solving process.

Main loop is like this. It types a word, then checks vertically to verify until its done.
