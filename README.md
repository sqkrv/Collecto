# Collecto

best project btw 

some changes because I can


\
Some options for board randomization:
1. Presets\
Find >5 preset boards and select one randomly.
   
2. Randomized selection\
Take 8 balls of each colour, select a random ball, insert
   it into the next spot on the field, check for neighbours
    + doesn't need a preset field
    - messy code
    - may cause problems near the end
   
3. Randomized injection\
Take 8 balls of each colour, for each colour, inject the balls 
   randomly onto the field, check for neighbours, swap if needed.
   + clean code
    - requires preset field
    - may cause problems at the end
    
4. Randomized board\
Fill up a board randomly with 8 balls of each colour, check if 
   the board is correct, retry if incorrect
   + simple
    - probably very inefficient
   
5. Row by row\
Fill an array with one ball of each colour (6 total), select one randomly
   and place it at (0,0) move on to (0,1) and pull from the resulting array.
   repeat for all rows, then the last column, excluding the middle ball 
   + simple
   + quick
   