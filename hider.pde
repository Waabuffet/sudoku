class Hider{
  Cell[][] copyGrid, backupGrid;
  int count;
  Diagonaler diagonaler;
  int difficulty;
  
  final int[] DIFFICULTIES = {40, 45, 50, 55};
  
  Hider(int count, Diagonaler diagonaler, int difficulty){
    this.count = count;
    this.diagonaler = diagonaler;
    this.difficulty = difficulty;
    copyGrid = new Cell[count][count];
    backupGrid = new Cell[count][count];
    initializeGrids();
  }
  
  void hideCellsRandomly(Cell[][] grid){
    //backup
    copyGrid(grid, backupGrid);
    
    do {
      //backup and restore backup
      copyGrid(backupGrid, grid);
      
      for(int i = 0; i < DIFFICULTIES[difficulty - 1]; i++){
        int randI = floor(random(0, count));
        int randJ = floor(random(0, count));
        while(grid[randI][randJ].number == 0){
          randI = floor(random(0, count));
          randJ = floor(random(0, count));
        }
        grid[randI][randJ].setNumber(0);
      }
      
      //copy to test on another grid
      copyGrid(grid, copyGrid);
    }while(!checkFeasibility());
    
    print("feasible");
  }
  
  void hideCellsRandomSymetricY(Cell[][] grid){
    //backup
    copyGrid(grid, backupGrid);
    
    do {
      //backup and restore backup
      copyGrid(backupGrid, grid);
      
      for(int i = 0; i < DIFFICULTIES[difficulty - 1] / 2; i++){
        int randI = floor(random(0, count / 2));
        int randJ = floor(random(0, count / 2));
        while(grid[randI][randJ].number == 0){
          randI = floor(random(0, count / 2));
          randJ = floor(random(0, count / 2));
        }
        grid[randI][randJ].setNumber(0);
        grid[randI][randJ + (2 * (4 - randI))].setNumber(0);
      }
      copyGrid(grid, copyGrid);
    }while(!checkFeasibility());
    
    print("feasible");
  }
  
  void initializeGrids(){
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        copyGrid[i][j] = new Cell(0,0,0,i,j);
      }
    }
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        backupGrid[i][j] = new Cell(0,0,0,i,j);
      }
    }
  }
  
  void copyGrid(Cell[][] grid, Cell[][] grid2){
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        grid2[i][j].setNumber(grid[i][j].number);
      }
    }
  }
  
  boolean checkFeasibility(){
    return diagonaler.recursiveBacktracker(copyGrid, count);
  }
  
  
}
