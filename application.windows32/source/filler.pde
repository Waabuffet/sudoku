class Filler{
  
  int[][] easynums = {{0,0,7,0,5,0,0,2,3},
                    {0,6,0,0,0,0,0,9,7},
                    {0,0,2,7,4,0,8,1,0},
                    {0,0,4,9,0,0,0,8,1},
                    {7,0,0,1,0,4,0,0,2},
                    {9,8,0,0,0,2,6,0,0},
                    {0,7,9,0,3,5,1,0,0},
                    {2,4,0,0,0,0,0,6,0},
                    {8,1,0,0,9,0,2,0,0}};
                    
  int[][] meduiumnums = {
                        {0,0,8,0,6,0,0,0,2},
                        {7,0,2,0,8,0,3,0,5},
                        {1,0,3,0,0,0,0,0,0},
                        {3,0,1,0,0,8,0,0,0},
                        {9,0,0,1,0,6,0,0,8},
                        {0,0,0,5,0,0,4,0,1},
                        {0,0,0,0,0,0,9,0,6},
                        {8,0,6,0,2,0,5,0,4},
                        {4,0,0,0,7,0,8,0,0}};
                    
  int[][] hardnums = {
                      {2,0,3,0,9,0,0,6,0},
                      {0,0,0,7,2,0,4,0,0},
                      {0,0,1,0,0,6,0,0,0},
                      {0,5,0,0,0,0,6,0,8},
                      {6,0,0,0,1,0,0,0,2},
                      {7,0,9,0,0,0,0,3,0},
                      {0,0,0,8,0,0,1,0,0},
                      {0,0,2,0,6,5,0,0,0},
                      {0,8,0,0,7,0,3,0,4}};
                    
  int[][] hardnums2 = {
                      {0,0,2,0,0,0,1,8,3},
                      {0,0,0,2,0,0,0,4,0},
                      {0,5,0,0,0,8,0,0,7},
                      {2,4,3,0,0,0,0,1,0},
                      {0,0,0,0,9,0,0,0,0},
                      {0,9,0,0,0,0,6,5,2},
                      {4,0,0,8,0,0,0,3,0},
                      {0,8,0,0,0,6,0,0,0},
                      {7,3,6,0,0,0,8,0,0}};
                    
  int[][] evilnums = {
                      {0,0,0,4,6,0,0,7,0},
                      {0,0,0,0,0,2,9,0,3},
                      {0,0,0,0,0,0,0,4,5},
                      {4,8,0,0,9,0,0,0,0},
                      {2,0,0,1,0,8,0,0,7},
                      {0,0,0,0,2,0,0,8,1},
                      {8,2,0,0,0,0,0,0,0},
                      {1,0,5,7,0,0,0,0,0},
                      {0,3,0,0,4,9,0,0,0}};
  
  
  void fillGrid(Cell[][] grid, int count){
    
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        grid[i][j].setNumber(evilnums[i][j]);
      }
    }
  }
  
  void countEmpty(){
    int counter = 0;
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        if(easynums[i][j] == 0){
          counter++;
        }
      }
    }
    print("\ncount " + counter);
  }
}